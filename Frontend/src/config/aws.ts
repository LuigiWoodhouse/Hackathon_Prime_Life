export const awsConfig = {
  region: process.env.NEXT_PUBLIC_AWS_REGION || 'us-east-1',
  userPoolId: process.env.NEXT_PUBLIC_AWS_USER_POOL_ID || '1kmeueit3eqv5te1ublnq2vhpa',
  clientId: process.env.NEXT_PUBLIC_AWS_CLIENT_ID || '1kmeueit3eqv5te1ublnq2vhpa',
} as const;

// Validate AWS configuration
if (!awsConfig.userPoolId || !awsConfig.clientId) {
  console.warn('AWS Cognito configuration is missing. Falling back to mock authentication.');
} 