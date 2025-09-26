-- Tạo ENUM cho giới tính
CREATE TYPE gender_enum AS ENUM ('male', 'female', 'other');

-- Tạo ENUM cho trạng thái user
CREATE TYPE status_user AS ENUM ('active', 'inactive', 'banned');

-- Tạo ENUM cho loại poster
CREATE TYPE poster_enum AS ENUM ('image', 'video', 'text');

-- Tạo ENUM cho reaction
CREATE TYPE reaction_enum AS ENUM ('like', 'love', 'haha', 'wow', 'sad', 'angry');


-- Bảng user
CREATE TABLE "user" (
    user_id BIGINT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    email VARCHAR(255) UNIQUE,
    full_name VARCHAR(255),
    refresh_token TEXT,
    gender gender_enum,
    dob DATE,
    status_user status_user,
    create_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    update_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    role_id BIGINT
);

-- Bảng role
CREATE TABLE role (
    role_id BIGINT PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL
);

-- Bảng permission
CREATE TABLE permission (
    permission_id BIGINT PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL
);

-- Bảng role_permission
CREATE TABLE role_permission (
    role_permission_id BIGINT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role(role_id),
    FOREIGN KEY (permission_id) REFERENCES permission(permission_id)
);

-- Bảng photo
CREATE TABLE photo (
    photo_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    url TEXT,
    filename VARCHAR(255),
    metadata JSONB,
    deleted_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);

-- Bảng album
CREATE TABLE album (
    album_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);

-- Bảng album_photo (N-N giữa album và photo)
CREATE TABLE album_photo (
    album_photo_id BIGINT PRIMARY KEY,
    album_id BIGINT NOT NULL,
    photo_id BIGINT NOT NULL,
    FOREIGN KEY (album_id) REFERENCES album(album_id),
    FOREIGN KEY (photo_id) REFERENCES photo(photo_id)
);

-- Bảng shared_album
CREATE TABLE shared_album (
    shared_album_id BIGINT PRIMARY KEY,
    album_id BIGINT NOT NULL,
    shared_with BIGINT NOT NULL, -- user_id được chia sẻ
    permission VARCHAR(20),
    FOREIGN KEY (album_id) REFERENCES album(album_id),
    FOREIGN KEY (shared_with) REFERENCES "user"(user_id)
);

-- Bảng poster
CREATE TABLE poster (
    poster_id BIGINT PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    file_path VARCHAR(255),
    poster_type poster_enum,
    create_date DATE DEFAULT CURRENT_DATE,
    update_date DATE,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_draft BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    post_style_id BIGINT,
    photo_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES "user"(user_id),
    FOREIGN KEY (photo_id) REFERENCES photo(photo_id)
);

-- Bảng poster_reaction
CREATE TABLE poster_reaction (
    reaction_id BIGINT PRIMARY KEY,
    poster_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    reaction reaction_enum,
    create_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    FOREIGN KEY (poster_id) REFERENCES poster(poster_id),
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);
