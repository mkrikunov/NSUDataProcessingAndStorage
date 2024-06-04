@staticmethod
def load_query(filename: str) -> str:
    with open(filename, "r") as file:
        query = file.read()
    return query
