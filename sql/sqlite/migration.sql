BEGIN;
INSERT INTO person(id, created, modified, version, name, gender) VALUES
  (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Петр', 'M'),
  (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Тамара', 'F'),
  (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Иван', 'M'),
  (4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'Ольга', 'F');

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

INSERT INTO person_photo(id, created, modified, version, data, person_id)
SELECT 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, NULL, (SELECT id FROM person WHERE name = 'Иван');

COMMIT;
