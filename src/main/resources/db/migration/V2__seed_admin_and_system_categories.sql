-- Insert ADMIN role
INSERT INTO role (role_id, role_name)
VALUES ('11111111-1111-1111-1111-111111111111', 'ADMIN')
ON CONFLICT (role_name) DO NOTHING;

-- Insert admin user (password: admin123 → BCrypt)
-- Generate real hash using: new BCryptPasswordEncoder().encode("admin123")
INSERT INTO app_user (
    user_id,
    username,
    password,
    email,
    role_id,
    created_at
) VALUES (
    '22222222-2222-2222-2222-222222222222',
    'admin',
    '$2a$10$3t0c3g3r4t3d3m0d3y3s3u3v3w3x3y3z3a3b3c3d3e3f3g3h3i3j3k', -- BCrypt: admin123
    'admin@finance.local',
    '11111111-1111-1111-1111-111111111111',
    NOW()
) ON CONFLICT (username) DO NOTHING;

-- System categories (user_id = NULL)
INSERT INTO category (category_id, category_name, category_type, user_id)
VALUES
    ('33333333-3333-3333-3333-333333333333', 'Salary',        'INCOME',  NULL),
    ('44444444-4444-4444-4444-444444444444', 'Freelance',     'INCOME',  NULL),
    ('55555555-5555-5555-5555-555555555555', 'Investment',    'INCOME',  NULL),
    ('66666666-6666-6666-6666-666666666666', 'Rent',          'EXPENSE', NULL),
    ('77777777-7777-7777-7777-777777777777', 'Groceries',     'EXPENSE', NULL),
    ('88888888-8888-8888-8888-888888888888', 'Transport',     'EXPENSE', NULL),
    ('99999999-9999-9999-9999-999999999999', 'Entertainment', 'EXPENSE', NULL)
ON CONFLICT DO NOTHING;