from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
import time

from sqlalchemy import create_engine, Column, String, Integer, Text, DateTime
from sqlalchemy.orm import sessionmaker, declarative_base
from datetime import datetime
import os
from dotenv import load_dotenv

# .env 환경변수 로드
load_dotenv()

# DB 세팅
DATABASE_URL = os.getenv("DATABASE_URL")
engine = create_engine(DATABASE_URL, echo=False, future=True)
SessionLocal = sessionmaker(bind=engine)
Base = declarative_base()

# 뉴스 테이블 모델 정의
class News(Base):
    __tablename__ = "news"
    id = Column(Integer, primary_key=True, autoincrement=True)
    title = Column(String(500), nullable=False)
    source = Column(String(100), nullable=True)
    date = Column(DateTime, nullable=True)
    content = Column(Text)
    url = Column(String(1000), nullable=False, unique=True)
    image_url = Column(String(2000), nullable=True)
    thema = Column(String(100), nullable=True)

Base.metadata.create_all(engine)

# 셀레니움 설정
chromedriver_path =os.getenv('CHROMEDRIVER_PATH')
service = Service(chromedriver_path)

options = Options()
options.add_argument('--headless')
options.add_argument('--no-sandbox')
options.add_argument('--disable-dev-shm-usage')

driver = webdriver.Chrome(service=service, options=options)

# 1. 다음 경제 뉴스 메인 페이지 접속
driver.get("https://news.daum.net/economy")
time.sleep(3)

# 2. 기사 링크(상위 30개) 수집
news_items = driver.find_elements(By.CSS_SELECTOR, "ul.list_newsheadline2 > li")[:30]
links = []
for item in news_items:
    try:
        a_tag = item.find_element(By.CSS_SELECTOR, "a.item_newsheadline2")
        link = a_tag.get_attribute("href")
        links.append(link)
    except:
        continue

session = SessionLocal()

# 3. 상세 페이지 크롤링 및 저장
for idx, link in enumerate(links, 1):
    driver.get(link)
    time.sleep(2)

    try:
        title = driver.find_element(By.CSS_SELECTOR, "h3.tit_view").text
    except:
        title = "제목 없음"

    try:
        source = driver.find_element(By.CSS_SELECTOR, "a#kakaoServiceLogo").text
    except:
        source = None

    try:
        date_str = driver.find_element(By.CSS_SELECTOR, "span.num_date").text
        date = datetime.strptime(date_str, "%Y. %m. %d. %H:%M")
    except:
        date = None

    try:
        image = driver.find_element(By.CSS_SELECTOR, "img.thumb_g_article").get_attribute("data-org-src")
    except:
        image = None

    try:
        paragraphs = driver.find_elements(By.CSS_SELECTOR, "p[dmcf-ptype='general']")
        content = "\n".join([p.text for p in paragraphs if p.text.strip() != ""])
    except:
        content = None

    exists = session.query(News).filter(News.url == link).first()
    if exists:
        print(f"[{idx}] 이미 저장된 기사입니다: {title}")
        continue

    news_item = News(
        title=title,
        source=source,
        date=date,
        content=content,
        url=link,
        image_url=image,
        thema=None
    )
    session.add(news_item)
    session.commit()

    print(f"[{idx}] 저장 완료: {title}")

session.close()
driver.quit()
