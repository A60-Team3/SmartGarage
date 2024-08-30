use smart_garage;

-- Insert roles
INSERT INTO roles (user_role) VALUES
                                  ('CUSTOMER'),
                                  ('CLERK'),
                                  ('MECHANIC'),
                                  ('HR');

-- Insert service types
INSERT INTO service_types (service_name, service_price) VALUES
                                                            ('Oil Change', 29.99),
                                                            ('Tire Rotation', 19.99),
                                                            ('Brake Inspection', 39.99),
                                                            ('Engine Diagnostics', 89.99);

-- Insert users (with bcrypt hashed passwords)
-- Password: 'password123' (hashed using bcrypt)
INSERT INTO users (first_name, last_name, email, username, password, phone_number) VALUES
                                                                                       ('John', 'Doe', 'john.doe@example.com', 'johndoe', '$2a$10$wHsfhg1.uCZTvnPR3ZUp/O0wFn4/n/hQ8vV3gZ39sDwIOBqPSGZNy', '1234567890'),
                                                                                       ('Jane', 'Smith', 'jane.smith@example.com', 'janesmith', '$2a$10$wHsfhg1.uCZTvnPR3ZUp/O0wFn4/n/hQ8vV3gZ39sDwIOBqPSGZNy', '0987654321'),
                                                                                       ('Bob', 'Johnson', 'bob.johnson@example.com', 'bobjohnson', '$2a$10$wHsfhg1.uCZTvnPR3ZUp/O0wFn4/n/hQ8vV3gZ39sDwIOBqPSGZNy', '1122334455'),
                                                                                       ('Alice', 'Williams', 'alice.williams@example.com', 'alicewilliams', '$2a$10$wHsfhg1.uCZTvnPR3ZUp/O0wFn4/n/hQ8vV3gZ39sDwIOBqPSGZNy', '5566778899');

-- Assign roles to users
INSERT INTO users_roles (user_id, role_id) VALUES
                                               (1, 1), -- John Doe as CUSTOMER
                                               (2, 2), -- Jane Smith as CLERK
                                               (3, 3), -- Bob Johnson as MECHANIC
                                               (4, 4); -- Alice Williams as HR

-- Insert vehicle brands
INSERT INTO vehicle_brands (brand_name) VALUES
                                            ('Toyota'),
                                            ('Honda'),
                                            ('Ford'),
                                            ('Chevrolet');

-- Insert vehicle models
INSERT INTO vehicle_models (model_name) VALUES
                                            ('Camry'),
                                            ('Civic'),
                                            ('F-150'),
                                            ('Malibu');

-- Insert vehicle years
INSERT INTO vehicle_years (year) VALUES
                                     (2020),
                                     (2019),
                                     (2018),
                                     (2017);

-- Insert model-brand associations
INSERT INTO models_brands (model_id, brand_id) VALUES
                                                   (1, 1), -- Camry is a Toyota
                                                   (2, 2), -- Civic is a Honda
                                                   (3, 3), -- F-150 is a Ford
                                                   (4, 4); -- Malibu is a Chevrolet

-- Insert production year-model associations
INSERT INTO production_years_models (year_id, model_id) VALUES
                                                            (1, 1), -- 2020 Camry
                                                            (2, 2), -- 2019 Civic
                                                            (3, 3), -- 2018 F-150
                                                            (4, 4); -- 2017 Malibu

-- Insert vehicles
INSERT INTO vehicles (license_plate, VIN, brand_id, model_id, year_id, employee_id, owner_id) VALUES
                                                                                                  ('ABC1234', '1HGCM82633A123456', 1, 1, 1, 2, 1), -- Toyota Camry 2020 owned by John Doe, managed by Jane Smith
                                                                                                  ('XYZ5678', '1HGCM82633A654321', 2, 2, 2, 3, 2), -- Honda Civic 2019 owned by Jane Smith, managed by Bob Johnson
                                                                                                  ('LMN8901', '1HGCM82633A789012', 3, 3, 3, 4, 3), -- Ford F-150 2018 owned by Bob Johnson, managed by Alice Williams
                                                                                                  ('DEF3456', '1HGCM82633A345678', 4, 4, 4, 1, 4); -- Chevrolet Malibu 2017 owned by Alice Williams, managed by John Doe

-- Insert visits
INSERT INTO visits (schedule_date, customer_id, employee_id, vehicle_id, status) VALUES
                                                                                     ('2024-08-01', 1, 2, 1, 'NOT_STARTED'),
                                                                                     ('2024-08-05', 2, 3, 2, 'IN_PROGRESS'),
                                                                                     ('2024-08-10', 3, 4, 3, 'READY_FOR_PICKUP'),
                                                                                     ('2024-08-15', 4, 1, 4, 'NOT_STARTED');

-- Insert services
INSERT INTO services (visit_id, service_type_id) VALUES
                                                     (1, 1), -- Oil Change for visit 1
                                                     (1, 2), -- Tire Rotation for visit 1
                                                     (2, 3), -- Brake Inspection for visit 2
                                                     (3, 4); -- Engine Diagnostics for visit 3

-- Insert logs for visits
INSERT INTO logs (description, visit_id) VALUES
                                             ('Customer arrived at the shop', 1),
                                             ('Oil change started', 1),
                                             ('Tire rotation completed', 1),
                                             ('Brake inspection started', 2),
                                             ('Engine diagnostics completed', 3);