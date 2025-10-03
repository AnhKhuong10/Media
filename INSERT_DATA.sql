INSERT INTO role (role_id, role_name)
VALUES 
    (1, 'director'),
    (2, 'hr'),
    (3, 'employee');

INSERT INTO "user" (
    user_id, username, password, phone, email, full_name, refresh_token,
    gender, dob, status_user, create_date, update_date, role_id, avatar
) VALUES
    (1, 'giamdoc01', '$2a$12$xE7j3tT4/w8vzi/tdjOIQOoQ.JHTVHJ8B7FAxpiIdSN6u7iYwlJ/.', '0901234567', 'giamdoc@company.com', 'Nguyễn Văn Giám Đốc', NULL,
     'male', '1980-01-01', 'active', NOW(), NOW(), 1, '/images/giamdoc01.png'),

    (2, 'hr01', '$2a$12$xE7j3tT4/w8vzi/tdjOIQOoQ.JHTVHJ8B7FAxpiIdSN6u7iYwlJ/.', '0902234567', 'hr@company.com', 'Trần Thị HR', NULL,
     'female', '1990-05-10', 'active', NOW(), NOW(), 2, '/images/hr01.png'),

    (3, 'employee01', '$2a$12$xE7j3tT4/w8vzi/tdjOIQOoQ.JHTVHJ8B7FAxpiIdSN6u7iYwlJ/.', '0903234567', 'employee1@company.com', 'Lê Văn Nhân Viên', NULL,
     'male', '1995-08-20', 'active', NOW(), NOW(), 3, '/images/employee01.png');

