# GlowCorner Backend API 🚀

A robust Spring Boot REST API for the GlowCorner e-commerce platform, handling user authentication, product management, order processing, and payment integration.

## ✨ Features

- **User Management**: Registration, authentication with JWT tokens
- **Product Catalog**: CRUD operations for cosmetics and beauty products
- **Order Processing**: Complete order lifecycle management
- **Payment Integration**: Stripe payment processing
- **Discount System**: Promotional codes and discount calculations
- **File Upload**: Product images with Cloudinary integration
- **Email Services**: Order confirmations and notifications
- **Security**: JWT-based authentication and authorization
- **API Documentation**: Swagger/OpenAPI integration

## 🛠️ Technology Stack

- **Spring Boot 3.x**: Main framework
- **Spring Security**: Authentication and authorization
- **Spring Data MongoDB**: Database operations
- **JWT**: Token-based authentication
- **Stripe API**: Payment processing
- **Cloudinary**: Image storage and management
- **Swagger**: API documentation
- **Maven**: Dependency management
- **MongoDB**: NoSQL database

## 📊 Database Schema

### Collections:
- **Users**: User accounts and profiles
- **Products**: Product catalog with categories
- **Orders**: Order management with status tracking
- **OrderDetails**: Individual order items
- **Promotions**: Discount codes and campaigns

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- MongoDB 4.4+
- IDE (IntelliJ IDEA recommended)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/glowcorner-backend.git
   cd glowcorner-backend
   ```

2. Configure application properties:
   ```properties
   # Database Configuration
   spring.data.mongodb.uri=mongodb://localhost:27017/glowcorner

   # JWT Configuration
   jwt.secret=your-secret-key
   jwt.expiration=86400000

   # Stripe Configuration
   stripe.api.key=your-stripe-secret-key

   # Cloudinary Configuration
   cloudinary.cloud-name=your-cloud-name
   cloudinary.api-key=your-api-key
   cloudinary.api-secret=your-api-secret

   # Email Configuration
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   ```

3. Install dependencies:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`

## 📖 API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

### Main Endpoints:

#### Authentication
- `POST /auth/login` - User login
- `POST /auth/register` - User registration

#### Products
- `GET /api/products` - Get all products
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

#### Orders
- `GET /api/orders/staff` - Get all orders (staff)
- `GET /api/orders/staff/{orderId}` - Get order details
- `PUT /api/orders/staff/{orderId}` - Update order status
- `POST /api/orders` - Create new order

#### Users
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile

## 🔧 Configuration

### Environment Variables
Create a `.env` file or set environment variables:
```bash
MONGODB_URI=mongodb://localhost:27017/glowcorner
JWT_SECRET=your-jwt-secret-key
STRIPE_SECRET_KEY=sk_test_...
CLOUDINARY_URL=cloudinary://...
```

### Security Configuration
The API uses JWT tokens for authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## 📦 Project Structure

```
src/main/java/com/glowcorner/backend/
├── config/              # Configuration classes
├── controller/          # REST controllers
├── entity/              # Database entities
├── enums/               # Enumerations
├── model/               # DTOs and request/response models
├── repository/          # Data access layer
├── security/            # Security configuration
├── service/             # Business logic
└── utils/               # Utility classes
```

## 🔗 Related Projects

- **Mobile App**: [GlowCorner Mobile](https://github.com/yourusername/glowcorner-mobile)

## 🧪 Testing

Run tests with:
```bash
mvn test
```

## 🚀 Deployment

### Docker Deployment
```bash
# Build Docker image
docker build -t glowcorner-backend .

# Run container
docker run -p 8080:8080 glowcorner-backend
```

### Cloud Deployment
The application is ready for deployment on:
- Heroku
- AWS Elastic Beanstalk
- Google Cloud Platform
- Azure App Service

## 🤝 Contributing

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

- **Project Link**: [https://github.com/yourusername/glowcorner-backend](https://github.com/yourusername/glowcorner-backend)
- **Mobile App**: [https://github.com/yourusername/glowcorner-mobile](https://github.com/yourusername/glowcorner-mobile)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- MongoDB for the flexible database solution
- Stripe for payment processing capabilities
