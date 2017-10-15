BEGIN;

CREATE TABLE poll (
  `id`         VARCHAR(36)  NOT NULL,
  `title`      VARCHAR(255) NOT NULL,
  `category`   VARCHAR(255) NOT NULL DEFAULT '',
  `end_date`   TIMESTAMP    NOT NULL DEFAULT now(),
  `start_date` TIMESTAMP    NOT NULL DEFAULT now(),
  PRIMARY KEY (`id`)
);

CREATE TABLE question (
  id          VARCHAR(36)  NOT NULL PRIMARY KEY,
  title       VARCHAR(255) NOT NULL,
  description TEXT,
  poll_id     VARCHAR(36)  NOT NULL,
  FOREIGN KEY (poll_id) REFERENCES poll (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE answer (
  id          VARCHAR(36)  NOT NULL PRIMARY KEY,
  title       VARCHAR(255) NOT NULL,
  number      INT DEFAULT 0,
  question_id VARCHAR(36)  NOT NULL,
  FOREIGN KEY (question_id) REFERENCES question (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE user (
  id       VARCHAR(36)  NOT NULL PRIMARY KEY,
  fio      VARCHAR(255) NOT NULL,
  birthday DATE         NOT NULL,
  sex      VARCHAR(36)  NOT NULL DEFAULT 'М'
);

CREATE TABLE link (
  answer_id   VARCHAR(36) NOT NULL,
  question_id VARCHAR(36) NOT NULL,
  user_id     VARCHAR(36) NOT NULL,
  PRIMARY KEY (user_id, question_id, answer_id),
  FOREIGN KEY (question_id) REFERENCES question (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (user_id) REFERENCES user (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (answer_id) REFERENCES answer (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

COMMIT;