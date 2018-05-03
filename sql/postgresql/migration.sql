BEGIN;
INSERT INTO person(created, modified, version, name, gender) VALUES
  (now(), now(), 1, 'Петр', 'M'),
  (now(), now(), 1, 'Тамара', 'F'),
  (now(), now(), 1, 'Иван', 'M'),
  (now(), now(), 1, 'Ольга', 'F');

INSERT INTO person_parent(person_id, parent_id)
SELECT p.id, r.id
FROM person p
  LEFT JOIN person r ON r.name = 'Иван'
WHERE  p.name= 'Петр';

INSERT INTO person_parent(person_id, parent_id)
SELECT p.id, r.id
FROM person p
  LEFT JOIN person r ON r.name = 'Ольга'
WHERE  p.name= 'Петр';

INSERT INTO person_parent(person_id, parent_id)
SELECT p.id, r.id
FROM person p
  LEFT JOIN person r ON r.name = 'Иван'
WHERE  p.name= 'Тамара';

INSERT INTO person_photo(created, modified, version, data, person_id)
SELECT now(), now(), 1, import_bytea('/Users/s.ustinov/Repos/bpctest/photo.png'), (SELECT id FROM person WHERE name = 'Иван');

COMMIT;
