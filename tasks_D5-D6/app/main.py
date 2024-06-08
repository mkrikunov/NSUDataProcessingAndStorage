import uvicorn
from fastapi import FastAPI
from app.database import database
from app.routers import cities, airports, schedules, bookings, checkin

app = FastAPI()


@app.on_event("startup")
async def startup():
    await database.connect()


@app.on_event("shutdown")
async def shutdown():
    await database.disconnect()


app.include_router(cities.router, prefix="/api", tags=["cities"])
app.include_router(airports.router, prefix="/api", tags=["airports"])
app.include_router(schedules.router, prefix="/api", tags=["schedules"])
app.include_router(bookings.router, prefix="/api", tags=["bookings"])
app.include_router(checkin.router, prefix="/api", tags=["checkin"])

uvicorn.run(app)

