import json
import random

from translate import Translator
from app.models import Airport, ScheduleIn, ScheduleOut


def load_query(filename: str) -> str:
    with open(filename, "r") as file:
        query = file.read()
    return query


def translate_text(text, src_lang='en', dest_lang='ru'):
    translator = Translator(from_lang=src_lang, to_lang=dest_lang)
    return translator.translate(text)


def parse_airport(row):
    return Airport(
        airport_code=row['airport_code'],
        airport_name=json.loads(row['airport_name']),
        city=json.loads(row['city']),
        coordinates=f"{row['coordinates'].x}, {row['coordinates'].y}",
        timezone=row['timezone']
    )


def generate_passenger_id():
    part1 = random.randint(1000, 9999)
    part2 = random.randint(100000, 999999)
    return f"{part1} {part2}"


def parse_schedule(row, flag):
    if flag == "is_inbound":
        return ScheduleIn(
            day_of_week=row['day_of_week'].strip(),
            arrival_time=row['arrival_time'],
            flight_no=row['flight_no'],
            origin=row['origin']
        )
    if flag == "is_outbound":
        return ScheduleOut(
            day_of_week=row['day_of_week'].strip(),
            departure_time=row['departure_time'],
            flight_no=row['flight_no'],
            destination=row['destination']
        )


def generate_ticket_no():
    random_number = random.randint(0, 9999999999999)
    return f"{random_number:013}"
