import os
from typing import List

from fastapi import APIRouter, HTTPException
from app.database import database
from app.models import Schedule
from app.routers import utils

router = APIRouter()

get_inbound_schedule_for_an_airport_file = (
    os.path.join(os.path.dirname(__file__), "../../resources/sql/schedules/get_inbound_schedule_for_an_airport.sql"))
get_outbound_schedule_for_an_airport_file = (
    os.path.join(os.path.dirname(__file__), "../../resources/sql/schedules/get_outbound_schedule_for_an_airport.sql"))


@router.get("/airport/{airport_code}/inbound_schedule", response_model=List[Schedule], responses={
    200: {"description": "Successful response. List of inbound schedule for the airport."},
    404: {"description": "No inbound schedule found for the airport."},
    500: {"description": "Internal server error."}
})
async def get_inbound_schedule(airport_code: str):
    try:
        query = utils.load_query(get_inbound_schedule_for_an_airport_file)
        results = await database.fetch_all(query, values={"airport_code": airport_code})
        if not results:
            raise HTTPException(status_code=404,
                                detail="No inbound schedule found for the airport.")
        return [Schedule(**row) for row in results]
    except Exception as e:
        raise HTTPException(status_code=500, detail="Internal server error.")


@router.get("/airport/{airport_code}/outbound_schedule", response_model=List[Schedule], responses={
    200: {"description": "Successful response. List of outbound schedule for the airport."},
    404: {"description": "No outbound schedule found for the airport."},
    500: {"description": "Internal server error."}
})
async def get_outbound_schedule(airport_code: str):
    try:
        query = utils.load_query(get_outbound_schedule_for_an_airport_file)
        results = await database.fetch_all(query, values={"airport_code": airport_code})
        if not results:
            raise HTTPException(status_code=404,
                                detail="No outbound schedule found for the airport.")
        return [Schedule(**row) for row in results]
    except Exception as e:
        raise HTTPException(status_code=500, detail="Internal server error.")
