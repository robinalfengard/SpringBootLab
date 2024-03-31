INSERT  INTO user(id, name, username, email, profile_picture)
VALUES (1, 'Robin Rydenberg', 'Anders','RD@gmail.com', 'https://robohash.org/1')
     , (2, 'Eriak Andersson', 'Hanna','RDB@gmail.com', 'https://robohash00.org/1');

INSERT INTO message(is_public, timestamp, user_id, text, title)
VALUES (1,CURRENT_TIMESTAMP(), 1 ,'Our world is a breathtaking tapestry of wonders, from the majestic peaks of towering mountains to the tranquil shores of crystal-clear oceans. Each sunrise paints the sky with a symphony of colors, while lush forests whisper tales of ancient wisdom. Diverse cultures intertwine, weaving a rich tapestry of traditions and beliefs. In every corner, nature''s beauty beckons, reminding us of the precious gift of life on this magnificent planet.','The Beauty of Our World')
     , (1,CURRENT_TIMESTAMP(), 2,'Exploring Earth unveils a kaleidoscope of marvels, where cascading waterfalls dance amidst emerald forests and vibrant coral reefs teem with life beneath azure waves. From the serene tranquility of sun-kissed meadows to the rugged grandeur of desert landscapes, our planet''s beauty knows no bounds. Each sunset paints a masterpiece across the horizon, a testament to the awe-inspiring artistry of nature. Amidst this boundless beauty, we find solace and inspiration, forever captivated by the enchanting allure of our world.','Discovering the Splendor of Earth');