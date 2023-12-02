from typing import Union
import base64
from pydantic import BaseModel

from fastapi import FastAPI
from fastapi import HTTPException
from fastapi import Body


from fastapi import FastAPI, UploadFile, File
from tensorflow.keras.models import model_from_json
from tensorflow.keras.models import Sequential
from tensorflow.keras import layers
from tensorflow.keras.optimizers import Adam
from tensorflow.keras import Input
from tensorflow.keras.models import Model
from tensorflow.keras.applications import EfficientNetB2
import tensorflow_addons as tfa


from PIL import Image
import numpy as np
import io


from fastapi.middleware.cors import CORSMiddleware

import tensorflow as tf

import cx_Oracle
from typing import Optional


from fastapi import Depends, HTTPException


app = FastAPI(debug=True)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 모델을 저장할 변수
model = None


@app.post("/apply_weights/")
def load_model(modelPath: str = Body(...)):
    print("testCOmCOM")
    global model

    # EfficientNetB2 모델 생성
    base_model = EfficientNetB2(
        input_shape=(224, 224, 3),
        include_top=False,  # 마지막 fully-connected layer는 제외
        weights=None,
    )
    model = Sequential(
        [
            base_model,
            layers.GlobalAveragePooling2D(),
            layers.Dense(1, activation="sigmoid"),
        ]
    )

    model.load_weights(modelPath)

    return {"message": "모델 로드 완료"}


class ImageData(BaseModel):
    encoded_image_data: str


@app.post("/predict/")
async def predict(image_data: ImageData):
    # Base64 인코딩된 이미지 데이터를 디코딩하여 바이트 데이터로 변환
    try:
        # Base64 인코딩된 이미지 데이터를 디코딩하여 바이트 데이터로 변환
        image_data = base64.b64decode(image_data.encoded_image_data)
    except Exception as e:
        print(f"Error decoding image data: {e}")
        raise

    # 바이트 데이터를 BytesIO 객체로 변환하여 이미지 파일로 읽음
    image = Image.open(io.BytesIO(image_data))

    # 이미지를 RGB로 변환하고, 사이즈를 (224, 224)로 조정
    image = image.convert("RGB")
    image = image.resize((224, 224))

    # 모델에 입력으로 주기 위해 numpy array로 변환
    img_array = np.array(image)

    # 모델에 입력하기 위해 배치 차원 추가
    img_array = np.expand_dims(img_array, axis=0)

    prediction = model.predict(img_array)

    # 분류 결과에 따라 문자열로 반환
    if prediction > 0.5:
        result = "정상"
    else:
        result = "불량"

    # PIL Image를 BytesIO 객체로 변환
    buffered = io.BytesIO()
    image.save(buffered, format="JPEG")

    # BytesIO 객체를 base64 문자열로 변환
    img_str = base64.b64encode(buffered.getvalue())

    # Python3에서는 base64 인코딩 결과가 bytes 타입으로 반환되므로, 이를 문자열로 변환
    img_str = img_str.decode("utf-8")

    return {"result": result, "score": float(prediction), "image": img_str}


from typing import List


class ImageType(BaseModel):
    encoded_image_data: str
    label: str  # 이미지의 라벨. '정상' 또는 '불량'


# 이미지 데이터와 라벨을 저장하는 리스트
normal_images = []
error_images = []


@app.post("/upload_image/")
async def upload_image(image: ImageType):
    # 이미지 데이터 디코딩
    image_data = base64.b64decode(image.encoded_image_data)
    # 이미지 파일로 읽기
    image_file = Image.open(io.BytesIO(image_data))
    # 이미지를 RGB로 변환하고, 사이즈를 (224, 224)로 조정
    image_file = image_file.convert("RGB")
    image_file = image_file.resize((224, 224))
    # 이미지 데이터와 라벨을 리스트에 추가
    if image.label == "normal":
        normal_images.append({"data": image_file, "label": image.label})
        # 이미지가 300장 이상이면 가장 오래된 이미지부터 제거
        while len(normal_images) > 300:
            normal_images.pop(0)
    elif image.label == "error":
        error_images.append({"data": image_file, "label": image.label})
        # 이미지가 300장 이상이면 가장 오래된 이미지부터 제거
        while len(error_images) > 300:
            error_images.pop(0)
    else:
        return {"message": "Invalid label"}
    return {"normal": f"{len(normal_images)}", "error": f"{len(error_images)}"}


@app.delete("/delete_images/")  # DELETE 메서드를 사용해 이미지 삭제 요청을 처리
async def delete_images():
    normal_images.clear()  # 리스트 비우기
    error_images.clear()
    return {"message": "삭제 완료."}


# 모델을 학습시키는 함수
def train_model(normal_images, error_images):
    # 이미지 데이터와 라벨을 준비
    images = [np.array(image["data"]) for image in normal_images + error_images]
    images = np.array(images)
    labels = [
        1 if image["label"] == "normal" else 0 for image in normal_images + error_images
    ]

    # 데이터를 numpy array로 변환
    images = np.array(images)
    labels = np.array(labels)

    # EfficientNetV2 모델 생성
    base_model = EfficientNetB2(
        weights="imagenet", include_top=False, input_tensor=Input(shape=(224, 224, 3))
    )
    model = Sequential(
        [
            base_model,
            layers.GlobalAveragePooling2D(),
            layers.Dense(1, activation="sigmoid"),
        ]
    )

    # 모델 컴파일
    model.compile(loss="binary_crossentropy", optimizer=Adam(), metrics=["accuracy"])

    # 모델 학습
    model.fit(images, labels, epochs=10, batch_size=32)

    return model


# 모델 이름을 받는 데이터 타입 정의
class ModelName(BaseModel):
    userId: str
    currentTime: str
    modelName: str


@app.post("/create_model/")
async def create_model(model_info: ModelName):
    # 각 리스트에 이미지가 최소 100장씩 있는지 확인
    if len(normal_images) < 100 or len(error_images) < 100:
        return {"message": "정상 이미지와 불량 이미지가 최소 100장씩 필요합니다!"}

    # 모델 학습
    model = train_model(normal_images, error_images)

    # 모델 저장
    model.save_weights(
        f"./models/{model_info.userId}_{model_info.modelName}_weights.h5"
    )

    model_file_path = f"./models/{model_info.userId}_{model_info.modelName}_weights.h5"

    my_dsn = cx_Oracle.makedsn("localhost", 1521, sid="xe")
    connection = cx_Oracle.connect(user="service", password="12345", dsn=my_dsn)

    # 커서 생성
    cursor = connection.cursor()

    # 시퀀스 값 생성
    cursor.execute("SELECT MODEL_SEQ.NEXTVAL FROM DUAL")
    model_idx = cursor.fetchone()[0]

    # SQL 쿼리 생성
    query = """
    INSERT INTO TBL_DEEP_MODEL (MODEL_IDX, MB_ID, MODEL_PATH, MODEL_DATE, MODEL_NAME)
    VALUES (:modelIdx, :userId, :model_file_path, :currentTime, :modelName)
    """
    # SQL 쿼리 실행
    cursor.execute(
        query,
        {
            "modelIdx": model_idx,
            "userId": model_info.userId,
            "model_file_path": model_file_path,
            "currentTime": model_info.currentTime,
            "modelName": model_info.modelName,
        },
    )

    # 변경사항 커밋
    connection.commit()

    return {"message": "모델 생성 완료!", "file_path": model_file_path}
