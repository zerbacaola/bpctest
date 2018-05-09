-- SERIAL IS too short for ID storing
BEGIN;
CREATE TABLE person (
  id        BIGSERIAL PRIMARY KEY NOT NULL,
  created   TIMESTAMP WITHOUT TIME ZONE,
  modified  TIMESTAMP WITHOUT TIME ZONE,
  version   INTEGER NOT NULL DEFAULT 0,
  name      VARCHAR (255) NOT NULL,
  gender    VARCHAR (1) NOT NULL
);
CREATE INDEX idx_person_name ON person(name);
CREATE INDEX idx_person_gender ON person(gender);

CREATE TABLE person_parent (
  person_id   BIGINT NOT NULL,
  parent_id   BIGINT NOT NULL
);

CREATE INDEX idx_person_parent_person_id ON person_parent(person_id);
CREATE INDEX idx_person_parent_parent_id ON person_parent(parent_id);

ALTER TABLE person_parent ADD CONSTRAINT fk_person_parent_person_id
FOREIGN KEY (person_id) REFERENCES person;
ALTER TABLE person_parent ADD CONSTRAINT fk_person_parent_parent_id
FOREIGN KEY (parent_id) REFERENCES person;

CREATE TABLE person_photo (
  id        BIGSERIAL PRIMARY KEY NOT NULL,
  created   TIMESTAMP WITHOUT TIME ZONE,
  modified  TIMESTAMP WITHOUT TIME ZONE,
  version   INTEGER NOT NULL DEFAULT 0,
  data      BYTEA,
  person_id BIGINT
);

CREATE INDEX idx_person_photo_person_id ON person_photo(person_id);
ALTER TABLE person_photo ADD CONSTRAINT fk_person_photo_person_id
FOREIGN KEY (person_id) REFERENCES person;

COMMIT;