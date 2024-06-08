import os

from fastapi import APIRouter, HTTPException
from app.database import database
from app.models import CheckInRequest
from app.routers.utils import load_query

router = APIRouter()

check_for_ticket_availability_file = (
    os.path.join(os.path.dirname(__file__), "../../resources/sql/checkin/check_for_ticket_availability.sql"))
check_for_flight_availability_file = (
    os.path.join(os.path.dirname(__file__), "../../resources/sql/checkin/check_for_flight_availability.sql"))
check_for_seat_availability_file = (
    os.path.join(os.path.dirname(__file__), "../../resources/sql/checkin/check_for_seat_availability.sql"))
update_boarding_passes_file = (
    os.path.join(os.path.dirname(__file__), "../../resources/sql/checkin/update_boarding_passes.sql"))


@router.post("/checkin")
async def online_check_in(checkin_request: CheckInRequest):

    # Проверка наличия билета и рейса:
    ticket_query = load_query(check_for_ticket_availability_file)
    flight_query = load_query(check_for_flight_availability_file)
    ticket_result = await database.fetch_one(ticket_query,
                                             values={"ticket_no": checkin_request.ticket_no})
    flight_result = await database.fetch_one(flight_query,
                                             values={"flight_id": checkin_request.flight_id})
    if not ticket_result:
        raise HTTPException(status_code=400, detail="Incorrect data")

    if not flight_result:
        raise HTTPException(status_code=400, detail="Flight not found.")

    # Проверка наличия указанного места:
    seat_query = load_query(check_for_seat_availability_file)

    seat_result = await database.fetch_one(seat_query, values={
        "aircraft_code": flight_result["aircraft_code"],
        "seat_no": checkin_request.seat_no,
        "ticket_no": checkin_request.ticket_no,
        "flight_id": checkin_request.flight_id
    })

    if not seat_result:
        raise HTTPException(status_code=404, detail="Seat not found or conditions mismatch.")

    # Обновление информации о посадочных талонах:
    checkin_query = load_query(update_boarding_passes_file)

    await database.execute(checkin_query, values={
        "ticket_no": checkin_request.ticket_no,
        "flight_id": checkin_request.flight_id,
        "seat_no": checkin_request.seat_no
    })

    return {"message": "Check-in successful."}
