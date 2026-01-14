# Shopify Analytics Dashboard 📊

A full-stack web application that connects to Shopify stores and provides real-time analytics, insights, and customer data visualization. Built as part of the Xeno FDE Internship Assignment 2025.

---

## 🌐 Live Demo

- **Frontend (Dashboard)**: [https://your-app.vercel.app](https://shopify-dashboard-frontend-five.vercel.app/)
- **Backend (API)**: [https://shopify-ingestion-production.up.railway.app/api]

> *Replace the above links with your actual deployment URLs*

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [System Architecture](#system-architecture)
- [Database Design](#database-design)
- [Installation & Setup](#installation--setup)
- [API Documentation](#api-documentation)
- [Deployment Guide](#deployment-guide)
- [Known Limitations](#known-limitations)
- [Future Enhancements](#future-enhancements)
- [Screenshots](#screenshots)

---

## 🎯 Overview

This project demonstrates a multi-tenant SaaS platform that helps Shopify store owners analyze their business data through an intuitive dashboard. The application automatically syncs data from Shopify stores and presents key metrics like revenue trends, top customers, and order analytics.

### What Problem Does It Solve?

Shopify store owners often struggle to get quick insights about their business performance. This application provides:
- Real-time sales analytics
- Customer spending patterns
- Order trend visualization
- Automated data synchronization from Shopify

---

## ✨ Features

### Core Features
- **User Authentication**: Secure email-based login and signup system
- **Multi-Tenant Architecture**: Support for multiple Shopify stores with isolated data
- **Automated Data Sync**: Scheduled synchronization every hour with Shopify stores
- **Real-Time Analytics**: 
  - Total customers, orders, and revenue
  - Average order value calculation
  - Top 5 customers by spending
  - Revenue trends over time
- **Interactive Dashboard**: 
  - Line charts for order trends
  - Bar charts for revenue analysis
  - Pie charts for order status distribution
  - Date range filtering for custom analysis
- **Manual Sync Option**: Trigger immediate data refresh from Shopify

### Technical Features
- **Multi-tenancy**: Each store's data is completely isolated using tenant identifiers
- **RESTful APIs**: Clean API design for all operations
- **Responsive Design**: Works seamlessly on desktop and mobile devices
- **Error Handling**: Graceful error management with user-friendly messages

---

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.4.12
- **Language**: Java 17
- **Database**: MySQL 8.0
- **ORM**: Hibernate (JPA)
- **Build Tool**: Maven
- **Scheduler**: Spring Task Scheduling

### Frontend
- **Framework**: React 18
- **Build Tool**: Vite / Create React App
- **HTTP Client**: Axios
- **Charts**: Recharts
- **Date Picker**: React DatePicker
- **Styling**: CSS3 with custom design

### Deployment
- **Backend Hosting**: Railway
- **Frontend Hosting**: Vercel
- **Database**: Railway MySQL
- **Version Control**: Git & GitHub

---

## 🏗️ System Architecture

```
┌─────────────────┐
│  Shopify Store  │ (External Data Source)
└────────┬────────┘
         │ REST API
         ↓
┌─────────────────────────────────┐
│   Spring Boot Backend           │
│  ┌──────────────────────────┐  │
│  │ Shopify Integration      │  │
│  │ Auto Sync (Every Hour)   │  │
│  └──────────────────────────┘  │
│  ┌──────────────────────────┐  │
│  │ Multi-Tenant Data Layer  │  │
│  └──────────────────────────┘  │
│  ┌──────────────────────────┐  │
│  │ Analytics Engine         │  │
│  └──────────────────────────┘  │
└────────┬────────────────────────┘
         │ REST API
         ↓
┌─────────────────────────────────┐
│   React Frontend                │
│  - Authentication               │
│  - Dashboard                    │
│  - Data Visualization           │
└─────────────────────────────────┘
```

### How It Works:

1. **User Registration**: Store owner creates account with Shopify credentials
2. **Data Ingestion**: Backend fetches customers, orders, and products from Shopify
3. **Storage**: Data is stored in MySQL database with tenant isolation
4. **Scheduled Updates**: Background job syncs data every hour automatically
5. **Analytics**: Backend processes data to generate insights
6. **Visualization**: Frontend displays analytics through interactive charts

---

## 🗄️ Database Design

### Multi-Tenant Structure

Every table includes a `tenant_id` column to ensure data isolation between different Shopify stores.

### Main Tables:

#### **tenants**
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Unique tenant identifier |
| tenant_name | VARCHAR | Store name |
| shopify_store_url | VARCHAR | Shopify store URL |
| shopify_access_token | VARCHAR | API access token (encrypted) |
| status | VARCHAR | ACTIVE or INACTIVE |
| created_at | TIMESTAMP | Account creation time |

#### **users**
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | User identifier |
| email | VARCHAR | Login email (unique) |
| password | VARCHAR | Hashed password |
| full_name | VARCHAR | User's full name |
| tenant_id | BIGINT (FK) | Links to tenant |
| role | VARCHAR | USER or ADMIN |
| created_at | TIMESTAMP | Registration time |

#### **customers**
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Customer identifier |
| tenant_id | BIGINT (FK) | Store association |
| shopify_customer_id | VARCHAR | Shopify's customer ID |
| email | VARCHAR | Customer email |
| first_name | VARCHAR | First name |
| last_name | VARCHAR | Last name |
| total_spent | DECIMAL | Total spending amount |
| orders_count | INTEGER | Number of orders |
| created_at | TIMESTAMP | Record creation time |

#### **orders**
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Order identifier |
| tenant_id | BIGINT (FK) | Store association |
| shopify_order_id | VARCHAR | Shopify's order ID |
| customer_id | BIGINT (FK) | Customer reference |
| order_number | VARCHAR | Order number (#1001) |
| total_price | DECIMAL | Order amount |
| currency | VARCHAR | Currency code |
| status | VARCHAR | Order status |
| order_date | TIMESTAMP | Order placement time |
| created_at | TIMESTAMP | Record creation time |

#### **products**
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Product identifier |
| tenant_id | BIGINT (FK) | Store association |
| shopify_product_id | VARCHAR | Shopify's product ID |
| title | VARCHAR | Product name |
| price | DECIMAL | Product price |
| inventory_quantity | INTEGER | Stock quantity |
| created_at | TIMESTAMP | Record creation time |

---

## 🚀 Installation & Setup

### Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- MySQL 8.0
- Git
- Maven

### Backend Setup

1. **Clone the repository**
```bash
git clone https://github.com/YOUR-USERNAME/shopify-ingestion.git
cd shopify-ingestion
```

2. **Configure MySQL Database**
```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE xeno_shopify;
```

3. **Update Configuration**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/xeno_shopify
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

shopify.store.url=YOUR-STORE.myshopify.com
shopify.access.token=YOUR_SHOPIFY_TOKEN
```

4. **Build and Run**
```bash
mvn clean install
mvn spring-boot:run
```

Backend will start at `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend folder**
```bash
cd shopify-dashboard
```

2. **Install dependencies**
```bash
npm install
```

3. **Configure API URL**

Create `.env` file:
```
REACT_APP_API_URL=http://localhost:8080/api
```

4. **Start development server**
```bash
npm start
```

Frontend will open at `http://localhost:3000`

---

## 📡 API Documentation

### Authentication Endpoints

#### **POST /api/auth/signup**
Create new user account and tenant.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "securepassword",
  "fullName": "John Doe",
  "storeName": "My Store",
  "storeUrl": "my-store.myshopify.com",
  "accessToken": "shpat_xxxxx"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Account created successfully",
  "userId": 1,
  "tenantId": 1
}
```

#### **POST /api/auth/login**
User authentication.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "securepassword"
}
```

**Response:**
```json
{
  "success": true,
  "userId": 1,
  "tenantId": 1,
  "fullName": "John Doe",
  "email": "user@example.com"
}
```

### Analytics Endpoints

#### **GET /api/analytics/summary/{tenantId}**
Get overall business metrics.

**Response:**
```json
{
  "totalCustomers": 150,
  "totalOrders": 320,
  "totalRevenue": 45678.50,
  "avgOrderValue": 142.75
}
```

#### **GET /api/analytics/top-customers/{tenantId}?limit=5**
Get top spending customers.

**Response:**
```json
[
  {
    "name": "John Doe",
    "email": "john@example.com",
    "totalSpent": 5420.00,
    "ordersCount": 12
  }
]
```

#### **GET /api/analytics/orders-by-date/{tenantId}**
Get order trends over time.

**Query Parameters:**
- `startDate` (optional): Filter start date (ISO format)
- `endDate` (optional): Filter end date (ISO format)

**Response:**
```json
[
  {
    "date": "2025-12-05",
    "revenue": 3450.50,
    "orderCount": 15
  }
]
```

#### **GET /api/analytics/order-status/{tenantId}**
Get order status distribution.

**Response:**
```json
[
  {
    "status": "FULFILLED",
    "count": 250
  },
  {
    "status": "PENDING",
    "count": 70
  }
]
```

### Sync Endpoints

#### **POST /api/sync/all/{tenantId}**
Manually trigger complete data synchronization.

**Response:**
```
Synced 150 customers successfully!
Synced 320 orders successfully!
Synced 85 products successfully!
```

---

## 🚢 Deployment Guide

### Backend Deployment (Railway)

1. **Create Railway account** at [railway.app](https://railway.app)
2. **Create new project** → Add MySQL database
3. **Note database credentials** from environment variables
4. **Deploy Spring Boot**:
   - Connect GitHub repository
   - Railway auto-detects Spring Boot
5. **Set environment variables**:
```
DATABASE_URL=jdbc:mysql://HOST:PORT/DATABASE
MYSQLUSER=username
MYSQLPASSWORD=password
SHOPIFY_STORE_URL=your-store.myshopify.com
SHOPIFY_ACCESS_TOKEN=shpat_xxxxx
```
6. **Deploy** - Railway builds and deploys automatically
7. **Get deployment URL** from Railway dashboard

### Frontend Deployment (Vercel)

1. **Create Vercel account** at [vercel.com](https://vercel.com)
2. **Import GitHub repository**
3. **Configure build settings**:
   - Framework: Create React App
   - Build Command: `npm run build`
   - Output Directory: `build`
4. **Add environment variable**:
```
REACT_APP_API_URL=https://your-backend.railway.app/api
```
5. **Deploy** - Vercel builds and deploys
6. **Get deployment URL** from Vercel dashboard

---

## ⚠️ Known Limitations

### Shopify API Constraints
- **Free tier limitation**: Shopify development stores on free plans restrict access to customer personal information (names, emails) via API
- **Workaround**: Customer data shows as "Guest Customer" for orders without full customer details
- **Impact**: Top customers may appear with limited information

### Data Synchronization
- **Frequency**: Automatic sync runs every hour
- **Manual sync**: Available but requires user action
- **Real-time updates**: Not implemented (webhooks not configured)

### Authentication
- **Simple implementation**: Basic password hashing without advanced security features
- **Production recommendation**: Implement OAuth 2.0 or JWT with refresh tokens

### Scalability
- **Single instance**: No load balancing configured
- **Database**: Single MySQL instance without replication
- **Caching**: Not implemented (removed for simplicity)

---

## 🔮 Future Enhancements

### Technical Improvements
1. **Implement Redis caching** for faster API responses
2. **Add RabbitMQ** for asynchronous data processing
3. **Webhook integration** for real-time Shopify updates
4. **Advanced authentication** with OAuth 2.0
5. **Unit and integration tests** for code reliability

### Feature Additions
1. **Export functionality** - Download data as CSV or PDF
2. **Email notifications** for sync failures or important events
3. **Advanced filtering** on all analytics views
4. **Product performance analytics** with detailed metrics
5. **Customer segmentation** based on behavior patterns
6. **Predictive analytics** using machine learning
7. **Mobile application** for iOS and Android

### Business Features
1. **Multiple user roles** (Admin, Viewer, Manager)
2. **Custom dashboard** configuration
3. **Automated reports** via email
4. **Integration with other platforms** (Amazon, eBay)

---

## 📸 Screenshots

### Login Page
<img width="1350" height="593" alt="image" src="https://github.com/user-attachments/assets/a4c72168-ad5f-4814-a9c3-cf2f86db92af" />

*Secure authentication with email and password*

### Signup Page
<img width="497" height="584" alt="image" src="https://github.com/user-attachments/assets/1fb169ee-e002-4c9c-a4f4-c46091390388" />

*Easy onboarding with Shopify store connection*

### Dashboard Overview
<img width="1365" height="468" alt="image" src="https://github.com/user-attachments/assets/1a8297f4-200c-4067-a328-f58e1d278317" />


### Revenue Trends
<img width="918" height="212" alt="image" src="https://github.com/user-attachments/assets/0c3d0a98-19b1-4b21-93e3-fad443d5a602" />

*Interactive charts showing business performance*

### Top Customers
<img width="1359" height="254" alt="image" src="https://github.com/user-attachments/assets/e026b003-daf8-4177-9ce0-64e0c23cc156" />

*List of highest spending customers*

---

## 🤝 Assignment Context

This project was developed as part of the **Xeno Forward Deployed Engineer (FDE) Internship Assignment - 2025**. The assignment requirements included:

- Multi-tenant Shopify data ingestion service
- Real-time analytics and insights dashboard
- Email-based authentication
- Data visualization with charts
- Production deployment
- Comprehensive documentation

### Requirements Met:
✅ Shopify store integration  
✅ Multi-tenant architecture  
✅ Automated data synchronization  
✅ Analytics APIs with date filtering  
✅ Top customers by spending  
✅ Interactive dashboard with charts  
✅ Authentication system  
✅ Deployed on cloud platforms  
✅ Complete documentation  

---

## 📝 Development Journey

### Challenges Faced:
1. **Multi-tenancy implementation** - Ensuring complete data isolation
2. **Shopify API limitations** - Handling free tier restrictions
3. **Data synchronization** - Managing scheduled jobs reliably
4. **CORS configuration** - Enabling frontend-backend communication
5. **Deployment complexities** - Environment variable management

### Key Learnings:
- Building production-ready multi-tenant systems
- Working with third-party APIs (Shopify)
- Full-stack application deployment
- Database design for SaaS applications
- Real-world problem-solving skills

---

## 👨‍💻 Author

**Your Name**  
Email: jaswanth5464@gmail.com  
LinkedIn: www.linkedin.com/in/jaswanth-kanamrlapudi-a41197252
GitHub: https://github.com/Jaswanth5464

---

## 📄 License

This project was created for educational purposes as part of an internship assignment.

---

## 🙏 Acknowledgments

- **Xeno** for providing this learning opportunity
- **Shopify** for comprehensive API documentation
- **Spring Boot** and **React** communities for excellent resources
- **Railway** and **Vercel** for free hosting platforms

---

## 📞 Support

For questions or issues:
- Open an issue on GitHub
- Contact via email: jaswanth5464@gmail.com

---

**Built with ❤️ for the Xeno FDE Internship - December 2025**
