# 📚 Bookstore E-Commerce Platform

> **Đề tài**: Xây dựng ứng dụng web bán sách trực tuyến  
> **Môn học**: Kiến Trúc và Thiết Kế Phần Mềm  
> **Trường**: Đại học Công nghiệp TP.HCM - Bộ Công Thương

## 🎯 Tổng Quan Dự Án

Hệ thống nhà sách trực tuyến (Bookstore) được phát triển nhằm đáp ứng nhu cầu tìm kiếm, chọn mua sách một cách nhanh chóng, tiện lợi, không giới hạn về thời gian và địa điểm. Dự án cung cấp một nền tảng hoàn chỉnh cho người dùng có thể duyệt, tìm kiếm và mua sách một cách dễ dàng, đồng thời hỗ trợ quản lý kho sách, đơn hàng và người dùng cho quản trị viên.

## 👥 Đội Ngũ Phát Triển

**Nhóm 12** - Sinh viên thực hiện:

| Thành viên | Vai trò | Chuyên môn |
|------------|---------|------------|
| **Tạ Lê Phú** | **Nhóm trưởng** | Backend Developer & System Architect |
| **Nguyễn Cao Trí** | Frontend Developer | Cart & Payment Features |
| **Trần Thị Quỳnh Như**  | UI/UX Designer | Interface Design & Product Management |
| **Nguyễn Văn Thạch** | Frontend Developer | User Management Features |

## 🏗️ Kiến Trúc Hệ Thống

### Layered Architecture Pattern

Dự án áp dụng **Layered Architecture (Kiến trúc phân lớp)** - một kiến trúc phổ biến giúp tách biệt rõ ràng các thành phần trong hệ thống, tăng tính dễ hiểu, dễ bảo trì và mở rộng.

```
┌─────────────────────────────────────┐
│         Presentation Layer          │  ← React.js Frontend
│            (Web App)                │
├─────────────────────────────────────┤
│          Controller Layer           │  ← API Controllers
│     (Request Handling & Routing)    │
├─────────────────────────────────────┤
│           Service Layer             │  ← Business Logic
│        (Business Logic)             │
├─────────────────────────────────────┤
│         Repository Layer            │  ← Data Access
│         (Data Access)               │
├─────────────────────────────────────┤
│          Database Layer             │  ← MySQL Database
│            (MySQL)                  │
└─────────────────────────────────────┘
```

### Nguyên Tắc Thiết Kế
- **Separation of Concerns**: Tách biệt trách nhiệm các layer
- **Scalability**: Dễ dàng mở rộng trong tương lai
- **Maintainability**: Dễ kiểm thử và bảo trì
- **Security**: Đảm bảo tính bảo mật

## 🚀 Tính Năng Hệ Thống

### 🌐 Guest (Người dùng chưa đăng nhập)
- **📖 Duyệt sách**: Xem danh sách và chi tiết sách
- **🔍 Tìm kiếm**: Tìm kiếm theo tên, tác giả, thể loại
- **🛒 Giỏ hàng tạm**: Thêm sách vào giỏ hàng tạm thời
- **📝 Đăng ký**: Tạo tài khoản mới

### 👤 Customer (Khách hàng)
- **🔐 Xác thực**: Đăng nhập/đăng xuất an toàn
- **🛍️ Mua sắm**: Quản lý giỏ hàng và thanh toán
- **👤 Hồ sơ**: Cập nhật thông tin cá nhân
- **📋 Lịch sử**: Xem lịch sử đơn hàng
- **🔑 Bảo mật**: Đổi mật khẩu và khôi phục tài khoản

### ⚙️ Admin (Quản trị viên)
- **📚 Quản lý sách**: CRUD operations cho sản phẩm
- **🏷️ Quản lý danh mục**: Quản lý thể loại sách
- **👥 Quản lý người dùng**: Phân quyền và quản lý tài khoản
- **📦 Quản lý đơn hàng**: Theo dõi và cập nhật trạng thái

## 🛠️ Công Nghệ Sử Dụng

### Frontend
- **Framework**: React.js
- **Build Tool**: Create React App
- **State Management**: React Hooks, Context API
- **Routing**: React Router
- **HTTP Client**: Axios
- **Styling**: CSS3, Responsive Design

### Backend
- **Framework**: Node.js/Express.js (dự đoán)
- **Database**: MySQL
- **Authentication**: JWT Token
- **Architecture**: RESTful API
- **ORM**: Database Repository Pattern

### Design & Documentation
- **UML Modeling**: Use Case, Class Diagram
- **Database Design**: Entity Relationship Diagram (ERD)
- **Architecture**: Layered Architecture Documentation

## 📊 Phân Tích & Thiết Kế

### UML Diagrams
- **Use Case Diagram**: Mô tả tương tác giữa 3 actors với hệ thống
- **Class Diagram**: Định nghĩa cấu trúc lớp đối tượng
- **ERD**: Thiết kế cơ sở dữ liệu quan hệ

### Work Breakdown Structure (WBS)
Dự án được phân chia thành các task cụ thể:
- Mỗi task: 2-8 giờ thực hiện
- Phân công rõ ràng cho từng thành viên
- Theo dõi tiến độ và chất lượng

## 🔗 Repository Links

| Component | Repository | Trách nhiệm |
|-----------|------------|-------------|
| **Frontend** | [bookstore_frontend](https://github.com/TaLePhu/bookstore_frontend) | React.js client application |
| **Backend** | [team12_project_software_architecture](https://github.com/TaLePhu/team12_project_software_architecture) | API server & business logic |

## 🎯 Phạm Vi Dự Án

### ✅ Bao gồm:
- **Core E-commerce**: Các chức năng cơ bản của nhà sách trực tuyến
- **Multi-user Support**: Hỗ trợ 3 loại người dùng khác nhau
- **Complete Workflow**: Từ browsing đến payment
- **Admin Management**: Quản lý toàn diện hệ thống

### ❌ Không bao gồm:
- Tích hợp AI gợi ý sách
- Chatbot hỗ trợ tự động
- Hệ thống vận chuyển thực tế
- Advanced analytics

## 🏆 Điểm Nổi Bật Kỹ Thuật

### Software Architecture
- ✅ **Layered Architecture**: Áp dụng pattern phân lớp chuẩn
- ✅ **Clean Code**: Separation of Concerns
- ✅ **Scalable Design**: Dễ dàng mở rộng
- ✅ **Maintainable**: Cấu trúc rõ ràng, dễ bảo trì

### Technical Skills
- ✅ **Full-stack Development**: Frontend + Backend integration
- ✅ **Database Design**: MySQL với ERD chuẩn
- ✅ **API Design**: RESTful API architecture
- ✅ **UML Modeling**: Phân tích hệ thống chuyên nghiệp
- ✅ **Team Collaboration**: Agile workflow với WBS

### Development Process
- ✅ **Requirements Analysis**: UML Use Case modeling
- ✅ **System Design**: Class diagram và ERD
- ✅ **Task Management**: WBS với phân công rõ ràng
- ✅ **Version Control**: Git workflow chuyên nghiệp

## 🚀 Demo & Installation

### Quick Start
```bash
# Clone repositories
git clone https://github.com/TaLePhu/bookstore_frontend.git
git clone https://github.com/TaLePhu/team12_project_software_architecture.git

# Setup Frontend
cd bookstore_frontend
git checkout develope
npm install
npm start

# Setup Backend (in separate terminal)
cd team12_project_software_architecture
npm install
npm start
```

### Access Points
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8000 (port có thể khác)
- **Admin Panel**: Integrated trong frontend

## 🎓 Giá Trị Học Thuật

### Kỹ Năng Được Rèn Luyện:
1. **Software Architecture**: Hiểu và áp dụng architectural patterns
2. **System Analysis**: Phân tích yêu cầu bằng UML
3. **Database Design**: Thiết kế CSDL quan hệ
4. **Full-stack Development**: Phát triển end-to-end
5. **Project Management**: Quản lý dự án với WBS
6. **Team Collaboration**: Làm việc nhóm hiệu quả

### Công Nghệ Tiên Tiến:
- Modern JavaScript (ES6+)
- React.js ecosystem
- RESTful API design
- MySQL database
- Responsive web design
- Git version control

## 💼 Năng Lực Thể Hiện:

**Technical Leadership:**
- Vai trò nhóm trưởng và system architect
- Thiết kế kiến trúc hệ thống từ đầu
- Phân tích yêu cầu và thiết kế UML

**Full-stack Expertise:**
- Frontend: React.js, responsive design
- Backend: API development, business logic
- Database: MySQL design và optimization
- Integration: Frontend-Backend communication

**Software Engineering:**
- Áp dụng design patterns trong thực tế
- Clean architecture và maintainable code
- Documentation và technical writing
- Testing và quality assurance

**Project Management:**
- Work Breakdown Structure (WBS)
- Task allocation và timeline management
- Team coordination (4 thành viên)
- Agile development methodology

### 📈 Quy Mô Dự Án:
- **15+ API endpoints** được thiết kế và implement
- **3 user roles** với workflows khác nhau
- **Complete e-commerce flow** từ browsing đến payment
- **Admin dashboard** với full CRUD operations
- **Responsive design** cho mọi thiết bị

### 🎯 Business Understanding:
- Phân tích domain e-commerce
- Hiểu workflow bán hàng online
- Xử lý business rules phức tạp
- User experience optimization

## 📞 Liên Hệ

**Tạ Lê Phú** - Nhóm trưởng
- **GitHub**: [@TaLePhu](https://github.com/TaLePhu)
- **Email**: [talephu6308@gmail.com]

## 📄 License

Dự án được phát triển cho mục đích học tập tại Trường Đại học Công nghiệp TP.HCM.

---

## 🌟 Kết Luận

Dự án **Bookstore E-Commerce Platform** thể hiện khả năng:
- **Phát triển ứng dụng full-stack** từ requirements đến deployment
- **Áp dụng architectural patterns** trong dự án thực tế
- **Làm việc nhóm chuyên nghiệp** với methodology rõ ràng
- **Tư duy hệ thống** và khả năng giải quyết vấn đề phức tạp

