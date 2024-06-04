from pydantic import BaseModel
from datetime import datetime
from typing import Optional


class City(BaseModel):
    city: str


class Airport(BaseModel):
    airport_code: str
    airport_name: str
    city: str
    coordinates: str
    timezone: str


class Schedule(BaseModel):
    day_of_week: str
    arrival_time: str
    flight_no: str
    origin: str


class BookingCreate(BaseModel):
    book_date: datetime
    total_amount: float
    passenger_id: str
    passenger_name: str
    contact_data: Optional[str]
    flight_id: str
    fare_conditions: str
    amount: float


class BookingResponse(BaseModel):
    book_ref: str
    ticket_no: str
    flight_id: str
    seat_no: str


class CheckInRequest(BaseModel):
    ticket_no: str
    flight_id: str
    seat_no: str