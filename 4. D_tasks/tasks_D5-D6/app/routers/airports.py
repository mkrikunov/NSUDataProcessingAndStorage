import json
import os
from typing import List

from fastapi import APIRouter, HTTPException
from app.database import database
from app.models import Airport
from app.routers.utils import load_query, parse_airport, translate_text

router = APIRouter()

get_airports_file = os.path.join(os.path.dirname(__file__), "../../resources/sql/airports/get_airports.sql")
get_airports_within_a_city_file = (os.path.join(os.path.dirname(__file__),
                                                "../../resources/sql/airports/get_airports_within_a_city.sql"))


@router.get("/airports", response_model=List[Airport], responses={
    200: {"description": "Successful response. List of airports."},
    404: {"description": "No airports found."},
    500: {"description": "Internal server error."}
})
async def get_airports():
    try:
        query = load_query(get_airports_file)
        results = await database.fetch_all(query)
        if not results:
            raise HTTPException(status_code=404, detail="No airports found")
        return [parse_airport(row) for row in results]
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.get("/airports/{city}", response_model=List[Airport], responses={
    200: {"description": "Successful response. List of airports within a city."},
    404: {"description": "No airports found in the specified city."},
    500: {"description": "Internal server error."}
})
async def get_airports_within_a_city(city: str):
    try:
        city_ru = translate_text(city, src_lang='en', dest_lang='ru')
        if '(' in city_ru and ')' in city_ru:
            start_index = city_ru.find('(') + 1
            end_index = city_ru.find(')')
            city_ru = city_ru[start_index:end_index]

        city_names = {"en": city, "ru": city_ru}
        city_query = json.dumps(city_names)
        query = load_query(get_airports_within_a_city_file)
        results = await database.fetch_all(query, values={"city": city_query})

        # Проверяем результаты запроса
        if not results:
            raise HTTPException(status_code=404, detail=city_names)

        # Возвращаем список аэропортов в городе
        return [parse_airport(row) for row in results]
    except Exception as e:
        raise e
