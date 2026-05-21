INSERT INTO member (email, password, name, role)
VALUES
    ('tester1@test.com', 'password1', 'tester1', 'USER'),
    ('tester2@test.com', 'password2', 'tester2', 'MANAGER');

INSERT INTO store (name, manager_id)
VALUES
    ('강남점', 2);

INSERT INTO reservation_time (start_time, end_time)
VALUES
    (DATEADD('DAY', -1, CURRENT_TIMESTAMP), DATEADD('DAY', -1, TIMESTAMPADD(HOUR, 1, CURRENT_TIMESTAMP))),
    (DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -2, TIMESTAMPADD(HOUR, 1, CURRENT_TIMESTAMP))),
    (DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -3, TIMESTAMPADD(HOUR, 1, CURRENT_TIMESTAMP)));

INSERT INTO theme (name, description, image_url, store_id)
VALUES
    ('미궁의 유산', '고대 미궁에서 탈출하세요.', 'https://example.com/themes/1.png', 1),
    ('시간의 균열', '시간이 무너지는 방을 구하세요.', 'https://example.com/themes/2.png', 1),
    ('심해 기지', '심해 기지의 비밀을 밝혀라.', 'https://example.com/themes/3.png', 1);

INSERT INTO reservation (member_id, time_id, theme_id)
VALUES
    (1, 1, 1),
    (1, 2, 1),
    (2, 3, 1),
    (1, 1, 2),
    (2, 2, 2),
    (1, 1, 3);