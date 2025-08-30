# 📚 Bookstore E-Commerce Platform

> **Đề tài**: Xây dựng ứng dụng web bán sách trực tuyến
> **Môn học**: Kiến Trúc và Thiết Kế Phần Mềm
> **Trường**: Đại học Công nghiệp TP.HCM - Bộ Công Thương

## 🎯 Tổng Quan Dự Án

Hệ thống **Bookstore** được phát triển nhằm đáp ứng nhu cầu tìm kiếm, chọn mua sách một cách nhanh chóng, tiện lợi, không giới hạn về thời gian và địa điểm.
Dự án cung cấp một nền tảng hoàn chỉnh cho người dùng có thể duyệt, tìm kiếm và mua sách dễ dàng, đồng thời hỗ trợ quản lý kho sách, đơn hàng và người dùng cho quản trị viên.

---

## 👥 Đội Ngũ Phát Triển

**Nhóm 12** - Sinh viên thực hiện:

| Thành viên             | Vai trò            | Chuyên môn                            |
| ---------------------- | ------------------ | ------------------------------------- |
| **Tạ Lê Phú**          | **Nhóm trưởng**    | Backend Developer & System Architect  |
| **Nguyễn Cao Trí**     | Frontend Developer | Cart & Payment Features               |
| **Trần Thị Quỳnh Như** | UI/UX Designer     | Interface Design & Product Management |
| **Nguyễn Văn Thạch**   | Frontend Developer | User Management Features              |

---

## 🏗️ Kiến Trúc Hệ Thống

### Layered Architecture Pattern

Dự án áp dụng **Layered Architecture (Kiến trúc phân lớp)** – tách biệt rõ ràng các thành phần trong hệ thống, tăng tính dễ bảo trì và mở rộng.

```
┌─────────────────────────────────────┐
│         Presentation Layer          │  ← React.js + TypeScript
│            (Web App)                │
├─────────────────────────────────────┤
│          Controller Layer           │  ← Spring Boot Controllers
│     (Request Handling & Routing)    │
├─────────────────────────────────────┤
│           Service Layer             │  ← Business Logic
│        (Business Logic)             │
├─────────────────────────────────────┤
│         Repository Layer            │  ← JPA/Hibernate
│         (Data Access)               │
├─────────────────────────────────────┤
│          Database Layer             │  ← MySQL Database
│            (MySQL)                  │
└─────────────────────────────────────┘
```

### Nguyên Tắc Thiết Kế

* **Separation of Concerns**: Tách biệt trách nhiệm từng layer
* **Scalability**: Dễ dàng mở rộng
* **Maintainability**: Dễ kiểm thử, dễ bảo trì
* **Security**: Đảm bảo tính bảo mật hệ thống

---

## 🚀 Tính Năng Hệ Thống

### 🌐 Guest (Người dùng chưa đăng nhập)

* 📖 Duyệt danh sách và chi tiết sách
* 🔍 Tìm kiếm theo tên, tác giả, thể loại
* 🛒 Giỏ hàng tạm (local storage)
* 📝 Đăng ký tài khoản

### 👤 Customer (Khách hàng)

* 🔐 Đăng nhập/đăng xuất an toàn (JWT)
* 🛍️ Quản lý giỏ hàng và thanh toán
* 👤 Cập nhật hồ sơ cá nhân
* 📋 Xem lịch sử đơn hàng
* 🔑 Đổi mật khẩu, khôi phục tài khoản

### ⚙️ Admin (Quản trị viên)

* 📚 CRUD sách
* 🏷️ Quản lý thể loại sách
* 👥 Quản lý người dùng và phân quyền
* 📦 Theo dõi & cập nhật trạng thái đơn hàng

---

## 🛠️ Công Nghệ Sử Dụng

### Frontend

* **Framework**: React.js + TypeScript
* **Build Tool**: Create React App
* **State Management**: React Hooks, Context API
* **Routing**: React Router v6
* **HTTP Client**: Axios
* **Styling**: CSS3, Responsive Design

### Backend

* **Framework**: Spring Boot
* **Build Tool**: Maven
* **Database**: MySQL
* **ORM**: JPA/Hibernate (Repository Pattern)
* **Authentication**: JWT Token
* **Architecture**: RESTful API

### Design & Documentation

* UML: Use Case Diagram, Class Diagram
* Database Design: ERD (Entity Relationship Diagram)
* Software Architecture: Layered Architecture

---

## 📊 Phân Tích & Thiết Kế

* **Use Case Diagram**: Mô tả tương tác 3 loại người dùng với hệ thống
* **Class Diagram**: Mô tả cấu trúc lớp
* **ERD**: Thiết kế cơ sở dữ liệu quan hệ
* **WBS (Work Breakdown Structure)**: Phân chia công việc thành các task (2–8 giờ), phân công rõ ràng cho từng thành viên

---

## 🔗 Repository Links

| Component    | Repository                                                                                                 | Mô tả                        |
| ------------ | ---------------------------------------------------------------------------------------------------------- | ---------------------------- |
| **Frontend** | [bookstore\_frontend](https://github.com/TaLePhu/bookstore_frontend)                                       | React.js + TypeScript Client |
| **Backend**  | [team12\_project\_software\_architecture](https://github.com/TaLePhu/team12_project_software_architecture) | Spring Boot API Server       |

---

## 🚀 Demo & Installation

### Quick Start

```bash
# Clone repositories
git clone https://github.com/TaLePhu/bookstore_frontend.git
git clone https://github.com/TaLePhu/team12_project_software_architecture.git
```

#### Setup Frontend

```bash
cd bookstore_frontend
git checkout develope
npm install
npm start
```

#### Setup Backend

```bash
cd team12_project_software_architecture
mvn clean install
mvn spring-boot:run
```

### Access Points

* **Frontend**: [http://localhost:3000](http://localhost:3000)
* **Backend API**: [http://localhost:8080](http://localhost:8080)
* **Admin Panel**: Tích hợp trong frontend

---

## 🎓 Giá Trị Học Thuật

### Kỹ Năng Được Rèn Luyện

1. Hiểu và áp dụng **Software Architecture Patterns**
2. Phân tích yêu cầu bằng UML
3. Thiết kế CSDL quan hệ MySQL
4. Phát triển **full-stack end-to-end**
5. Quản lý dự án với WBS và Git
6. Kỹ năng làm việc nhóm hiệu quả

### Công Nghệ Tiên Tiến

* React.js + TypeScript (CRA)
* RESTful API với Spring Boot
* JWT Authentication
* ORM với JPA/Hibernate
* MySQL database
* Git Workflow

---

## 💼 Năng Lực Thể Hiện

* **Technical Leadership**: Thiết kế kiến trúc hệ thống, UML, phân công nhóm
* **Full-stack Expertise**: ReactJS + Spring Boot + MySQL
* **Software Engineering**: Design patterns, clean architecture, testing
* **Project Management**: Agile, WBS, timeline tracking

---

## 📞 Liên Hệ

**Tạ Lê Phú** – Nhóm trưởng

* GitHub: [@TaLePhu](https://github.com/TaLePhu)
* Email: [talephu6308@gmail.com](mailto:talephu6308@gmail.com)

---

## 📄 License

Dự án được phát triển cho mục đích học tập tại Trường Đại học Công nghiệp TP.HCM.

---

## 🌟 Kết Luận

Dự án **Bookstore E-Commerce Platform** thể hiện khả năng:

* Phát triển ứng dụng **full-stack** từ yêu cầu đến triển khai
* Áp dụng **Layered Architecture** trong thực tế
* **Làm việc nhóm chuyên nghiệp** với workflow rõ ràng
* Tư duy hệ thống & giải quyết vấn đề phức tạp

---

👉 Bản này đã chuẩn theo stack thực tế của nhóm.

Bạn có muốn mình viết thêm **hướng dẫn cấu hình database MySQL (schema, user, password)** trong phần backend setup để người khác dễ chạy dự án hơn không?
