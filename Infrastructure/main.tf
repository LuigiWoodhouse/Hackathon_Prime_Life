provider "aws" {
  region = "us-east-1"
}

# Get the current AWS region
data "aws_region" "current" {}

# S3 Bucket Configuration
resource "aws_s3_bucket" "frontend" {
  bucket = "prime-life-frontend-app"
}

# S3 Website Configuration
resource "aws_s3_bucket_website_configuration" "frontend_website" {
  bucket = aws_s3_bucket.frontend.bucket

  index_document {
    suffix = "index.html"
  }

  error_document {
    key = "error.html"
  }
}

# Block Public Access (important for S3 security)
resource "aws_s3_bucket_public_access_block" "frontend_public_access" {
  bucket = aws_s3_bucket.frontend.id

  block_public_acls       = true  
  block_public_policy     = false  
  ignore_public_acls      = true  
  restrict_public_buckets = false  
}

# Bucket Policy for Public Read Access
resource "aws_s3_bucket_policy" "frontend_policy" {
  bucket = aws_s3_bucket.frontend.bucket
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect   = "Allow"
        Action   = "s3:GetObject"
        Resource = "${aws_s3_bucket.frontend.arn}/*"
        Principal = "*"
      }
    ]
  })

  depends_on = [aws_s3_bucket.frontend]
}

# Object Ownership Controls - Enforcing Bucket Owner Enforced
resource "aws_s3_bucket_ownership_controls" "frontend" {
  bucket = aws_s3_bucket.frontend.id
  rule {
    object_ownership = "BucketOwnerEnforced"
  }
}

# CloudFront Distribution Configuration
resource "aws_cloudfront_origin_access_control" "frontend_oac" {
  name                              = "frontend-oac"
  origin_access_control_origin_type = "s3"
  signing_behavior                  = "always"
  signing_protocol                  = "sigv4"
}

resource "aws_cloudfront_distribution" "frontend" {
  origin {
    domain_name                     = aws_s3_bucket.frontend.bucket_regional_domain_name
    origin_id                       = "S3Origin"
    origin_access_control_id        = aws_cloudfront_origin_access_control.frontend_oac.id
  }

  enabled             = true
  default_root_object = "index.html"

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  default_cache_behavior {
    allowed_methods  = ["GET", "HEAD"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "S3Origin"

    forwarded_values {
      query_string = false
      cookies {
        forward = "none"
      }
    }

    viewer_protocol_policy = "redirect-to-https"
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }
}

#AWS Cognito Pool Config
resource "aws_cognito_user_pool" "main" {
  name = "prime-life-user-pool"

  password_policy {
    minimum_length = 8
    require_uppercase = true
    require_numbers = true
    require_symbols = true
  }

  auto_verified_attributes = ["email"]
  
  tags = {
    Name = "PrimeLifeUserPool"
  }
}

resource "aws_cognito_user_pool_client" "app_client" {
  name         = "prime-life-app-client"
  user_pool_id = aws_cognito_user_pool.main.id

  generate_secret = false

  explicit_auth_flows = [
    "ALLOW_USER_PASSWORD_AUTH", 
    "ALLOW_USER_SRP_AUTH",
    "ALLOW_REFRESH_TOKEN_AUTH"
  ]

  allowed_oauth_flows        = ["code"]
  allowed_oauth_scopes       = ["openid", "email", "profile"]

  callback_urls = ["https://d33azkrw4vpadx.cloudfront.net/callback"]
  logout_urls   = ["https://d33azkrw4vpadx.cloudfront.net/logout"]


}


resource "aws_cognito_user_pool_domain" "example" {
  domain       = "prime-life"
  user_pool_id = aws_cognito_user_pool.main.id
}


# VPC Configuration (unchanged from original)
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}

# Public Subnet 1
resource "aws_subnet" "public_1" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-1a"
}

# Public Subnet 2
resource "aws_subnet" "public_2" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.2.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-1b"
}

# Private Subnet 1
resource "aws_subnet" "private_1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = "us-east-1a"
}

# Private Subnet 2
resource "aws_subnet" "private_2" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.4.0/24"
  availability_zone = "us-east-1b"
}

# Internet Gateway
resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.main.id
}

# Route Table for Public Subnets
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id
}

resource "aws_route" "public_internet_access" {
  route_table_id         = aws_route_table.public.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.gw.id
}

# Route Table Associations (Public Subnets)
resource "aws_route_table_association" "public_1" {
  subnet_id      = aws_subnet.public_1.id
  route_table_id = aws_route_table.public.id
}

resource "aws_route_table_association" "public_2" {
  subnet_id      = aws_subnet.public_2.id
  route_table_id = aws_route_table.public.id
}

# Route Table for Private Subnets
resource "aws_route_table" "private" {
  vpc_id = aws_vpc.main.id
}

# Route Table Associations (Private Subnets)
resource "aws_route_table_association" "private_1" {
  subnet_id      = aws_subnet.private_1.id
  route_table_id = aws_route_table.private.id
}

resource "aws_route_table_association" "private_2" {
  subnet_id      = aws_subnet.private_2.id
  route_table_id = aws_route_table.private.id
}

# Security Group for ALB
resource "aws_security_group" "alb_sg" {
  vpc_id = aws_vpc.main.id
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Security Group for ECS
resource "aws_security_group" "ecs_sg" {
  vpc_id = aws_vpc.main.id
  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }
}

# Security Group for RDS
resource "aws_security_group" "db_sg" {
  vpc_id = aws_vpc.main.id
  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.ecs_sg.id]
  }
}

resource "aws_iam_role" "ecs_execution_role" {
  name = "ecs_execution_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_execution_role_policy_attachment" {
  role       = aws_iam_role.ecs_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# ECR Repository
resource "aws_ecr_repository" "prime-life-app" {
  name = "prime-life-app"

  lifecycle {
    prevent_destroy = true
  }

  tags = {
    Name = "PrimeLifeAppRepository"
  }
}

# ECS Cluster
resource "aws_ecs_cluster" "ecs_cluster" {
  name = "primeLifeEcsCluster"
}

# ECS Task Definition for Spring Boot
resource "aws_ecs_task_definition" "app_task" {
  family                   = "primeLifeApp"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  memory                   = "512"
  cpu                      = "256"
  execution_role_arn       = aws_iam_role.ecs_execution_role.arn
  
  container_definitions = jsonencode([{
    name      = "springboot-app"
    image     = "${aws_ecr_repository.prime-life-app.repository_url}:latest" 
    cpu       = 256
    memory    = 512
    essential = true
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
      protocol      = "tcp"
    }]
  }])
}

# ECS Service with Load Balancer Configuration
resource "aws_ecs_service" "app_service" {
  name            = "primeLifeAppService"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.app_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"
  network_configuration {
    subnets          = [aws_subnet.public_1.id, aws_subnet.public_2.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.tg.arn
    container_name   = "springboot-app"
    container_port   = 8080
  }

  depends_on = [aws_lb_target_group.tg]
}

# Load Balancer Configuration
resource "aws_lb" "app_lb" {
  name               = "app-load-balancer"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = [aws_subnet.public_1.id, aws_subnet.public_2.id]
}

# Load Balancer Target Group
resource "aws_lb_target_group" "tg" {
  name         = "primeLifeTarget"
  port         = 8080
  protocol     = "HTTP"
  vpc_id       = aws_vpc.main.id
  target_type  = "ip" 

  health_check {
    interval            = 30
    path                = "/" 
    timeout             = 5
    healthy_threshold   = 3
    unhealthy_threshold = 3
  }
}


# Load Balancer Listener
resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.app_lb.arn
  port              = 80
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.tg.arn
  }
}

# DB Subnet Group
resource "aws_db_subnet_group" "main" {
  name       = "my-db-subnet-group"
  subnet_ids = [aws_subnet.private_1.id, aws_subnet.private_2.id]

  tags = {
    Name = "my-db-subnet-group"
  }
}

# RDS for PostgreSQL
resource "aws_db_instance" "db" {
  identifier           = "postgres-db"
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "postgres"
  engine_version       = "12"
  instance_class       = "db.t3.micro"
  username             = "dbadmin"
  password             = "password123"
  db_subnet_group_name = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.db_sg.id]
  publicly_accessible  = false
  skip_final_snapshot  = true
}