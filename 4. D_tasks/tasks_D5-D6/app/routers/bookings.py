import os
import uuid

from fastapi import APIRouter, HTTPException
from app.database import database
from app.models import BookingCreate, BookingResponse
from app.routers.utils import load_query, generate_passenger_id, generate_ticket_no

router = APIRouter()

check_flight_availability_file = os.path.join(os.path.dirname(__file__),
                                              "../../resources/sql/bookings/check_flight_availability.sql")
get_seat_on_aircraft_file = os.path.join(os.path.dirname(__file__),
                                         "../../resources/sql/bookings/get_seat_on_aircraft.sql")
get_new_boarding_no_file = os.path.join(os.path.dirname(__file__),
                                        "../../resources/sql/bookings/get_new_boarding_no.sql")

insert_into_bookings_file = os.path.join(os.path.dirname(__file__),
                                         "../../resources/sql/bookings/insert/insert_into_bookings.sql")
insert_into_tickets_file = os.path.join(os.path.dirname(__file__),
                                        "../../resources/sql/bookings/insert/insert_into_tickets.sql")
insert_into_ticket_flights_file = os.path.join(os.path.dirname(__file__),
                                               "../../resources/sql/bookings/insert/insert_into_ticket_flights.sql")
insert_into_boarding_passes_file = os.path.join(os.path.dirname(__file__),
                                                "../../resources/sql/bookings/insert/insert_into_boarding_passes.sql")


@router.post("/bookings", response_model=BookingResponse, responses={
    201: {"description": "Booking created successfully."},
    400: {"description": "No available seats for this flight."},
    404: {"description": "Flight not found."},
    500: {"description": "Internal server error."}
})
async def create_booking(booking: BookingCreate):
    try:
        book_ref = str(uuid.uuid4())[:6].upper()  # Создание уникального идентификатора бронирования
        ticket_no = generate_ticket_no()  # Создание уникального номера билета

        # Проверка наличия рейса:
        query_flight_check = load_query(check_flight_availability_file)
        flight_result = await database.fetch_one(query_flight_check, values={"flight_id": booking.flight_id})
        if not flight_result:
            raise HTTPException(status_code=404, detail="Flight not found")

        # Найти свободное место на рейсе:
        query_get_seat = load_query(get_seat_on_aircraft_file)
        seat_result = await database.fetch_one(query_get_seat, values={"flight_id": booking.flight_id})
        if seat_result is None:
            raise HTTPException(status_code=400, detail="No available seats for this flight")

        seat_no = seat_result['seat_no']

        # Получение максимального boarding_no для данного рейса и его увеличение на 1:
        query_max_boarding_no = load_query(get_new_boarding_no_file)
        max_boarding_no_result = await database.fetch_one(query_max_boarding_no,
                                                          values={"flight_id": booking.flight_id})
        new_boarding_no = max_boarding_no_result['new_boarding_no']

        async with database.transaction():
            # Вставка данных в таблицу bookings:
            await database.execute(load_query(insert_into_bookings_file), values={
                "book_ref": book_ref,
                "book_date": booking.book_date,
                "total_amount": booking.total_amount
            })
            # Вставка данных в таблицу tickets:
            await database.execute(load_query(insert_into_tickets_file), values={
                "ticket_no": ticket_no,
                "book_ref": book_ref,
                "passenger_id": generate_passenger_id(),
                "passenger_name": booking.passenger_name,
                "contact_data": booking.contact_data
            })
            # Вставка данных в таблицу ticket_flights:
            await database.execute(load_query(insert_into_ticket_flights_file), values={
                "ticket_no": ticket_no,
                "flight_id": booking.flight_id,
                "fare_conditions": booking.fare_conditions,
                "amount": booking.amount
            })

        return BookingResponse(
            book_ref=book_ref,
            ticket_no=ticket_no,
            flight_id=booking.flight_id,
            seat_no=seat_no
        )
    except HTTPException as e:
        raise e
    except Exception as e:
        raise e
