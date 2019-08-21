USE housing;
ALTER DATABASE housing DEFAULT CHARACTER SET utf8mb4;
DROP TABLE IF EXISTS institute CASCADE;
DROP TABLE IF EXISTS credit_guarantee CASCADE;
DROP TABLE IF EXISTS summary CASCADE;
DROP TABLE IF EXISTS user CASCADE;

CREATE TABLE institute (
  institute_id bigint(12) NOT NULL AUTO_INCREMENT,
  institute_name varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  institute_code varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (institute_id),
  KEY INSTITUTE_NAME_IDX (institute_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE credit_guarantee (
  credit_guarantee_id bigint(12) NOT NULL AUTO_INCREMENT,
  institute_id bigint(12) NOT NULL,
  year int(11) NOT NULL,
  month int(11) NOT NULL,
  amount bigint(10) NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (credit_guarantee_id),
  KEY INSTITUTE_ID_YEAR_MONTH_IDX (institute_id,year,month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE summary (
  summary_id bigint(12) NOT NULL AUTO_INCREMENT,
  year int(11) NOT NULL,
  institute_id bigint(12) NOT NULL,
  sum_amount bigint(10) NOT NULL,
  avg_amount bigint(10) NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (summary_id),
  KEY INSTITUTE_ID_YEAR_IDX (institute_id,year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user (
  user_id bigint(12) NOT NULL AUTO_INCREMENT,
  email varchar(90) COLLATE utf8mb4_unicode_ci NOT NULL,
  password varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  encrypt_key varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id),
  KEY EMAIL_IDX (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO institute (institute_name, institute_code)
VALUES
("주택도시기금", "HUF01"),
("국민은행", "KB02"),
("우리은행", "WR03"),
("신한은행", "SH04"),
("한국시티은행", "CT05"),
("하나은행", "HN06"),
("농협은행/수협은행", "NH07"),
("외환은행", "WH08"),
("기타은행", "OT09");

