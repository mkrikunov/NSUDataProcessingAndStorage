import os
from typing import List

from fastapi import APIRouter, HTTPException
from app.database import database
from app.models import Airport
from app.routers import utils

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
        query = utils.load_query(get_airports_file)
        results = await database.fetch_all(query)
        if not results:
            raise HTTPException(status_code=404, detail="No airports found")
        return [Airport(**row) for row in results]
    except Exception as e:
        raise HTTPException(status_code=500, detail="Internal server error.")


@router.get("/airports/{city}", response_model=List[Airport], responses={
    200: {"description": "Successful response. List of airports within a city."},
    404: {"description": "No airports found in the specified city."},
    500: {"description": "Internal server error."}
})
async def get_airports_within_a_city(city: str):
    try:
        query = utils.load_query(get_airports_within_a_city_file)
        results = await database.fetch_all(query, values={"city": city})
        if not results:
            raise HTTPException(status_code=404, detail=f"No airports found in the city '{city}'")
        return [Airport(**row) for row in results]
    except Exception as e:
        raise HTTPException(status_code=500, detail="Internal server error.")
