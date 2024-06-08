from pydantic import BaseModel
from datetime import datetime
from typing import Optional, Dict


class City(BaseModel):
    city: str


class Airport(BaseModel):
    airport_code: str
    airport_name: Dict[str, str]
    city: Dict[str, str]
    coordinates: str
    timezone: str

    #class Config:
    #    from_attributes = True


class ScheduleIn(BaseModel):
    day_of_week: str
    arrival_time: str
    flight_no: str
    origin: str


class ScheduleOut(BaseModel):
    day_of_week: str
    departure_time: str
    flight_no: str
    destination: str


class BookingCreate(BaseModel):
    book_date: datetime
    total_amount: float
    passenger_name: str
    contact_data: Optional[str]
    flight_id: int
    fare_conditions: str
    amount: float


class BookingResponse(BaseModel):
    book_ref: str
    ticket_no: str
    flight_id: int
    seat_no: str


class CheckInRequest(BaseModel):
    ticket_no: str
    flight_id: int
    seat_no: str
