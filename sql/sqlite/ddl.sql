-- SERIAL IS too short for ID storing
BEGIN;
CREATE TABLE person (
  id        SERIAL PRIMARY KEY NOT NULL,
  created   TIMESTAMP WITH TIME ZONE,
  modified  TIMESTAMP WITH TIME ZONE,
  version   INTEGER NOT NULL DEFAULT 0,
  name      VARCHAR (255) NOT NULL,
  gender    VARCHAR (1) NOT NULL
);
CREATE INDEX idx_person_name ON person(name);
CREATE INDEX idx_person_gender ON person(gender);

CREATE TABLE person_parent (
  person_id   BIGINT NOT NULL,
  parent_id   BIGINT NOT NULL,
  FOREIGN KEY (person_id) REFERENCES person(id),
  FOREIGN KEY (parent_id) REFERENCES person(id)
);

CREATE INDEX idx_person_parent_person_id ON person_parent(person_id);
CREATE INDEX idx_person_parent_parent_id ON person_parent(parent_id);

CREATE TABLE person_photo (
  id        SERIAL PRIMARY KEY NOT NULL,
  created   TIMESTAMP WITH TIME ZONE,
  modified  TIMESTAMP WITH TIME ZONE,
  version   INTEGER NOT NULL DEFAULT 0,
  data      BYTEA,
  person_id BIGINT,
  FOREIGN KEY (person_id) REFERENCES person(id)
);

CREATE INDEX idx_person_photo_person_id ON person_photo(person_id);

COMMIT;