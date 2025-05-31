INSERT INTO odometers (id, value, unit, status)
VALUES (1, 123, 'KILOMETERS','UNKNOWN');

---- Reset sequence to max id + 1
SELECT setval(pg_get_serial_sequence('odometers', 'id'), coalesce(max(id), 0) + 1, false) FROM odometers;

INSERT INTO lots (id, lot_number, auction, type, vin, odometer_id)
VALUES (1, 12345678, 'COPART', 'CAR', '0123456789ABC1234', 1);

---- Reset sequence to max id + 1
SELECT setval(pg_get_serial_sequence('lots', 'id'), coalesce(max(id), 0) + 1, false) FROM lots;
