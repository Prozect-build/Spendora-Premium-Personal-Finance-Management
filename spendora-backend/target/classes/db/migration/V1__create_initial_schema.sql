-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    otp_hash VARCHAR(255),
    otp_expiry TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_user_email ON users(email);

-- Create Categories Table
CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    icon VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL -- INCOME, EXPENSE, BOTH
);

-- Create Expenses Table
CREATE TABLE IF NOT EXISTS expenses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    date DATE NOT NULL,
    type VARCHAR(20) NOT NULL, -- INCOME, EXPENSE
    note TEXT,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_expense_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_expense_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX IF NOT EXISTS idx_expense_user_date ON expenses(user_id, date);

-- Create Budgets Table
CREATE TABLE IF NOT EXISTS budgets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    limit_amount DECIMAL(19, 2) NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    CONSTRAINT fk_budget_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_budget_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT uc_budget_user_category_date UNIQUE (user_id, category_id, month, year)
);

-- Pre-seed Categories (Postgres syntax)
INSERT INTO categories (name, icon, color, type) VALUES 
('Food', 'restaurant', '#FF5722', 'EXPENSE'),
('Shopping', 'shopping_bag', '#E91E63', 'EXPENSE'),
('Transport', 'directions_car', '#2196F3', 'EXPENSE'),
('Salary', 'payments', '#4CAF50', 'INCOME'),
('Rent', 'home', '#795548', 'EXPENSE'),
('Entertainment', 'movie', '#9C27B0', 'EXPENSE'),
('Freelance', 'work', '#009688', 'INCOME')
ON CONFLICT (name) DO NOTHING;
