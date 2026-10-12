DELETE FROM users WHERE user_name LIKE 'test.%';        -- remove previous fake rows
INSERT INTO users (user_name, password) VALUES
  ('test.frontdesk', SHA2('password123', 256)),
  ('test.nurse', SHA2('password123', 256)),
  ('test.doctor', SHA2('password123', 256));