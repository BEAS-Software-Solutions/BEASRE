#!/bin/bash

# Environment Variables Example for Beas Rule Engine
# This file shows how to set environment variables for secure configuration
# Copy this file to .env and modify the values for your environment

echo "Setting up environment variables for Beas Rule Engine..."

# =============================================================================
# CRYPTOGRAPHY CONFIGURATION
# =============================================================================
# Generate secure keys for production (32 characters for TripleDES)
export CRYPTO_KEY="your-secure-crypto-key-32-chars-long"
export CRYPTO_IV="your-iv-8chars"

# =============================================================================
# MONGODB CONFIGURATION
# =============================================================================
export MONGODB_HOST="your-mongodb-host"
export MONGODB_PORT="27017"
export MONGODB_DATABASE="beasre"
export MONGODB_USERNAME="your-mongodb-username"
export MONGODB_PASSWORD="your-mongodb-password"
export MONGODB_AUTH_DATABASE="admin"

# Alternative: Use MongoDB connection string
# export MONGODB_URI="mongodb://username:password@host:port/database?authSource=admin"

# =============================================================================
# KEYCLOAK/OAUTH2 CONFIGURATION
# =============================================================================
export KEYCLOAK_URL="https://your-keycloak-instance.com"
export KEYCLOAK_REALM="your-realm-name"

# =============================================================================
# SERVER CONFIGURATION
# =============================================================================
export SERVER_PORT="8070"
export SERVER_CONTEXT_PATH="/beasre/v1"

# =============================================================================
# APPLICATION CONFIGURATION
# =============================================================================
export APP_NAME="beasre"
export RULE_CONTAINER_NAME="general"

# =============================================================================
# FILE UPLOAD CONFIGURATION
# =============================================================================
export MAX_FILE_SIZE="1MB"
export MAX_REQUEST_SIZE="1MB"

# =============================================================================
# CONTACT INFORMATION
# =============================================================================
export CONTACT_EMAIL="your-contact@email.com"
export CONTACT_NAME="Your Company Name"
export CONTACT_URL="https://your-website.com"
export LICENSE_URL="https://your-website.com/license"
export TERMS_URL="https://your-website.com/terms"

echo "Environment variables set successfully!"
echo "You can now start the application with: java -jar beas-rule-engine.jar"
echo ""
echo "To verify the configuration, check the application logs."
echo "Remember to never commit actual secrets to version control!" 