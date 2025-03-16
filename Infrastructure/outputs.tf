output "cloudfront_url" {
  value = "https://${aws_cloudfront_distribution.frontend.domain_name}"
}

output "cognito_client_id" {
  value = aws_cognito_user_pool_client.app_client.id
}

output "cognito_hosted_ui_url" {
  value = "https://${aws_cognito_user_pool_domain.example.domain}.auth.${data.aws_region.current.name}.amazoncognito.com/login"
}
