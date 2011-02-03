def ColorDB:
    def __init__(db_filename):
        need_init = os.path.exists(db_filename)
        self.c = sqlite3.connect(db_filename)
        if need_init:
            