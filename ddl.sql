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
  parent_id   BIGINT NOT NULL
);

CREATE INDEX idx_person_parent_person_id ON person_parent(person_id);
CREATE INDEX idx_person_parent_parent_id ON person_parent(parent_id);

ALTER TABLE person_parent ADD CONSTRAINT fk_person_parent_person_id
FOREIGN KEY (person_id) REFERENCES person;
ALTER TABLE person_parent ADD CONSTRAINT fk_person_parent_parent_id
FOREIGN KEY (parent_id) REFERENCES person;

CREATE TABLE person_photo (
  id        SERIAL PRIMARY KEY NOT NULL,
  created   TIMESTAMP WITH TIME ZONE,
  modified  TIMESTAMP WITH TIME ZONE,
  version   INTEGER NOT NULL DEFAULT 0,
  data      BYTEA,
  person_id BIGINT
);

CREATE INDEX idx_person_photo_person_id ON person_photo(person_id);
ALTER TABLE person_photo ADD CONSTRAINT fk_person_photo_person_id
FOREIGN KEY (person_id) REFERENCES person;

CREATE OR REPLACE FUNCTION import_bytea(p_path text, p_result out bytea)
LANGUAGE plpgsql AS $$
  DECLARE
    l_oid oid;
  BEGIN
    SELECT lo_import(p_path) INTO l_oid;
    SELECT lo_get(l_oid) INTO p_result;
    PERFORM lo_unlink(l_oid);
  END;
$$;

COMMIT;