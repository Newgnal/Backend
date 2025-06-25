from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
import time
import os
from dotenv import load_dotenv  # 환경변수 불러오기
from sqlalchemy import create_engine, Column, String, Integer, Text, DateTime
from sqlalchemy.orm import sessionmaker, declarative_base
from datetime import datetime

# .env 로드
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
chromedriver_path = os.getenv('CHROMEDRIVER_PATH')
service = Service(chromedriver_path)

options = Options()
options.add_argument('--headless')
options.add_argument('--no-sandbox')
options.add_argument('--disable-dev-shm-usage')

driver = webdriver.Chrome(service=service, options=options)

# 네이버 경제 뉴스 메인 페이지 접속
driver.get("https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101")
time.sleep(3)


articles = driver.find_elements(By.CSS_SELECTOR, "a.sa_text_title")
links = [article.get_attribute("href") for article in articles[:30]]

session = SessionLocal()

for idx, link in enumerate(links, 1):
    driver.get(link)
    time.sleep(2)

    try:
        title = driver.find_element(By.CSS_SELECTOR, "h2#title_area").text
    except:
        title = "제목없음"

    try:
        source = driver.find_element(By.CSS_SELECTOR, "div.media_end_head_top img").get_attribute("alt")
    except:
        source = None

    try:
        date_elem = driver.find_element(By.CSS_SELECTOR, "span.media_end_head_info_datestamp_time._ARTICLE_DATE_TIME")
        date_str = date_elem.get_attribute("data-date-time")
        date = datetime.strptime(date_str, "%Y-%m-%d %H:%M:%S") if date_str else None
    except:
        date = None

    try:
        article = driver.find_element(By.CSS_SELECTOR, "article#dic_area")
        content = article.text
    except:
        content = None

    try:
        images = driver.find_elements(By.CSS_SELECTOR, "article#dic_area img")
        image_urls = [img.get_attribute("src") for img in images if img.get_attribute("src")]
    except:
        image_urls = []

    # 중복 체크
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
        image_url=image_urls[0] if image_urls else None,
        thema=None  # 현재는 thema 분류 안함
    )
    session.add(news_item)
    session.commit()

    print(f"[{idx}] 저장 완료: {title}")

session.close()
driver.quit()
