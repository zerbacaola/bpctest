BEGIN;
CREATE TABLE person (
  id        INTEGER PRIMARY KEY NOT NULL, -- 64 Bit
  created   TEXT, -- ISO8601 without Time Zone
  modified  TEXT, -- ISO8601 without Time Zone
  version   INTEGER NOT NULL DEFAULT 0,
  name      TEXT NOT NULL,
  gender    TEXT NOT NULL
) WITHOUT ROWID;
CREATE INDEX idx_person_name ON person(name);
CREATE INDEX idx_person_gender ON person(gender);

CREATE TABLE person_parent (
  person_id   INTEGER NOT NULL,
  parent_id   INTEGER NOT NULL,
  FOREIGN KEY (person_id) REFERENCES person(id),
  FOREIGN KEY (parent_id) REFERENCES person(id)
);

CREATE INDEX idx_person_parent_person_id ON person_parent(person_id);
CREATE INDEX idx_person_parent_parent_id ON person_parent(parent_id);

CREATE TABLE person_photo (
  id        INTEGER PRIMARY KEY NOT NULL, -- 64 Bit
  created   TEXT, -- ISO8601 without Time Zone
  modified  TEXT, -- ISO8601 without Time Zone
  version   INTEGER NOT NULL DEFAULT 0,
  data      BLOB,
  person_id INTEGER,
  FOREIGN KEY (person_id) REFERENCES person(id)
) WITHOUT ROWID;

CREATE INDEX idx_person_photo_person_id ON person_photo(person_id);

COMMIT;