CREATE TABLE roles
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_role ENUM ('CUSTOMER', 'CLERK', 'MECHANIC', 'HR') NOT NULL
);

CREATE TABLE users
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name   VARCHAR(32)                        NOT NULL,
    last_name    VARCHAR(32)                        NOT NULL,
    email        VARCHAR(50)                        NOT NULL UNIQUE,
    username     VARCHAR(50)                        NOT NULL UNIQUE,
    password     VARCHAR(1024)                      NOT NULL,
    phone_number VARCHAR(10)                        NOT NULL UNIQUE,
    registered   DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated      DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_active    BOOLEAN  DEFAULT 1
);

CREATE TABLE users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    constraint fk_users_roles_users
        foreign key (user_id) references users (id)
            ON DELETE CASCADE,
    constraint fk_users_roles_roles
        foreign key (role_id) references roles (id)
            ON DELETE CASCADE
);

CREATE TABLE profile_pictures
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    photo_url VARCHAR(255) NOT NULL
);

CREATE TABLE profile_pictures_users
(
    user_id  BIGINT NOT NULL,
    photo_id BIGINT NOT NULL,
    constraint fk_profile_pictures_users_users
        foreign key (user_id) references users (id)
            ON DELETE CASCADE,
    constraint fk_profile_pictures_users_profile_pictures
        foreign key (photo_id) references profile_pictures (id)
            ON DELETE CASCADE

);

CREATE TABLE production_years
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    year INT NOT NULL
);

CREATE TABLE vehicle_models
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    model_name VARCHAR(20) NOT NULL
);

CREATE TABLE vehicle_brands
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(20) NOT NULL
);

CREATE TABLE production_years_models
(
    year_id  BIGINT NOT NULL,
    model_id BIGINT NOT NULL,
    constraint fk_production_years_models_years
        foreign key (year_id) references production_years (id)
            ON DELETE CASCADE,
    constraint fk_production_years_models_models
        foreign key (model_id) references vehicle_models (id)
            ON DELETE CASCADE
);

CREATE TABLE models_brands
(

    model_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    constraint fk_models_brands_models
        foreign key (model_id) references vehicle_models (id)
            ON DELETE CASCADE,
    constraint fk_models_brands_brands
        foreign key (brand_id) references vehicle_brands (id)
            ON DELETE CASCADE
);

CREATE TABLE vehicles
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(8)  NOT NULL,
    VIN           VARCHAR(17) NOT NULL,
    brand_id      BIGINT      NOT NULL,
    employee_id   BIGINT      NOT NULL,
    owner_id      BIGINT      NOT NULL,
    added_on      DATE DEFAULT CURRENT_TIMESTAMP(),
    updated_on    DATE DEFAULT CURRENT_TIMESTAMP(),
    CONSTRAINT fk_vehicles_clerks
        FOREIGN KEY (employee_id) REFERENCES users (id),
    CONSTRAINT fk_vehicles_customers
        FOREIGN KEY (owner_id) REFERENCES users (id),
    CONSTRAINT fk_vehicles_brands
        FOREIGN KEY (brand_id) REFERENCES vehicle_brands (id)
);

CREATE TABLE service_types
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_type VARCHAR(50) NOT NULL
);

CREATE TABLE services
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    added_on   DATE DEFAULT CURRENT_TIMESTAMP(),
    updated_on DATE DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE services_service_types
(
    service_id      BIGINT NOT NULL,
    service_type_id BIGINT NOT NULL,
    constraint fk_services_service_types_services
        foreign key (service_id) references services (id)
            ON DELETE CASCADE,
    constraint fk_services_service_types_types
        foreign key (service_type_id) references service_types (id)
            ON DELETE CASCADE
);

CREATE TABLE logs
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(200) NOT NULL,
    timestamp   DATETIME DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE visits
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    scheduled_date DATE   NOT NULL,
    client_id      BIGINT NOT NULL,
    employee_id    BIGINT NOT NULL,
    vehicle_id     BIGINT NOT NULL,
    status         ENUM ('NOT_STARTED','IN_PROGRESS','READY_FOR_PICKUP'),
    booked_on      DATE DEFAULT CURRENT_TIMESTAMP(),
    updated_on     DATE DEFAULT CURRENT_TIMESTAMP(),
    constraint fk_visits_customers
        foreign key (client_id) references users (id)
            ON DELETE CASCADE,
    constraint fk_visits_clerks
        foreign key (employee_id) references users (id)
            ON DELETE CASCADE,
    constraint fk_visits_vehicles
        foreign key (vehicle_id) references vehicles (id)
            ON DELETE CASCADE
);

CREATE TABLE visits_logs
(
    visit_id BIGINT NOT NULL,
    log_id   BIGINT NOT NULL,
    constraint fk_visits_logs_visits
        foreign key (visit_id) references visits (id)
            ON DELETE CASCADE,
    constraint fk_visits_logs_logs
        foreign key (log_id) references logs (id)
            ON DELETE CASCADE
);



