import os
from typing import List

from fastapi import APIRouter, HTTPException
from app.database import database
from app.models import City
from app.routers.utils import load_query

router = APIRouter()

get_cities_file = os.path.join(os.path.dirname(__file__), "../../resources/sql/cities/get_cities.sql")


@router.get("/cities", response_model=List[City], responses={
    200: {"description": "Successful response. List of cities."},
    404: {"description": "No cities found."},
    500: {"description": "Internal server error."}
})
async def get_cities():
    try:
        query = load_query(get_cities_file)
        results = await database.fetch_all(query)
        if not results:
            raise HTTPException(status_code=404, detail="No cities found")
        return [City(city=row["city"]) for row in results]
    except Exception as e:
        raise HTTPException(status_code=500, detail="Internal server error.")
