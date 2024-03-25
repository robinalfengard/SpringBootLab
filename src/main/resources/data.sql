INSERT  INTO user(first_name, last_name, username, email, profile_picture)
VALUES ( 'Robin', 'Rydenberg', 'username1','RD@gmail.com', 'https://robohash.org/1')
, ( 'Eriak', 'Andersson', 'username2','RDB@gmail.com', 'https://robohash00.org/1');

INSERT INTO message(is_public, timestamp, user_id, text, title)
VALUES (1,CURRENT_TIMESTAMP(), 1 ,'text1','title1')
, (1,CURRENT_TIMESTAMP(), 2,'text2','title2');