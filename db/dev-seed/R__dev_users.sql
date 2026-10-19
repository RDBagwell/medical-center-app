DELETE FROM users WHERE user_name LIKE 'test.%';        -- remove previous fake rows
INSERT INTO users (user_name, password) VALUES
  ('test.frontdesk', '$2a$12$Coq5CoqZOmqlgh.bsSDpA.llzUzVgbo5Q/PYyHUPHXDy7wH4fJYuO'),
  ('test.nurse', '$2a$12$1lGfzWDstYlPfspjVDn7CuDcNYBnr7fxBvBDjtM1lv9OMGN2mV29W'),
  ('test.doctor', '$2a$12$MS4b8EDIWqAahAPOvpJNLO3xdc7SobzcyPXazVpjl1ysHkknPTi8C');