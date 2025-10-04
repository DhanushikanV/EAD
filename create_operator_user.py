#!/usr/bin/env python3
"""
Script to create an operator user for testing
This script creates a user with operator role for testing the mobile app
"""

import requests
import json
import sys

# API base URL
BASE_URL = "http://localhost:5263"

def create_operator_user():
    """Create an operator user via API"""
    
    # Operator user data
    operator_data = {
        "Username": "operator1",
        "Email": "operator@evcharging.com", 
        "PasswordHash": "operator123",
        "Role": "Operator",
        "Status": "Active"
    }
    
    try:
        print("Creating operator user...")
        response = requests.post(
            f"{BASE_URL}/api/user",
            json=operator_data,
            headers={"Content-Type": "application/json"},
            timeout=30
        )
        
        if response.status_code == 201:
            print("✅ Operator user created successfully!")
            print(f"Response: {response.text}")
            return True
        else:
            print(f"❌ Failed to create user. Status: {response.status_code}")
            print(f"Response: {response.text}")
            return False
            
    except requests.exceptions.RequestException as e:
        print(f"❌ Network error: {e}")
        return False

def test_login():
    """Test login with the created operator user"""
    
    login_data = {
        "Email": "operator@evcharging.com",
        "Password": "operator123"
    }
    
    try:
        print("\nTesting operator login...")
        response = requests.post(
            f"{BASE_URL}/api/user/login",
            json=login_data,
            headers={"Content-Type": "application/json"},
            timeout=30
        )
        
        if response.status_code == 200:
            result = response.json()
            print("✅ Login successful!")
            print(f"Token: {result.get('token', 'N/A')[:50]}...")
            print(f"User Role: {result.get('user', {}).get('role', 'N/A')}")
            print(f"User Status: {result.get('user', {}).get('status', 'N/A')}")
            return True
        else:
            print(f"❌ Login failed. Status: {response.status_code}")
            print(f"Response: {response.text}")
            return False
            
    except requests.exceptions.RequestException as e:
        print(f"❌ Network error: {e}")
        return False

def main():
    print("🚀 EV Charging Station - Operator User Creation Script")
    print("=" * 60)
    
    # Test if API is running
    try:
        response = requests.get(f"{BASE_URL}/swagger/index.html", timeout=5)
        if response.status_code == 200:
            print("✅ API is running")
        else:
            print("❌ API might not be running properly")
    except:
        print("❌ Cannot connect to API. Make sure the backend is running on port 5263")
        sys.exit(1)
    
    # Create operator user
    if create_operator_user():
        # Test login
        test_login()
    
    print("\n" + "=" * 60)
    print("📱 Mobile App Login Credentials:")
    print("Email: operator@evcharging.com")
    print("Password: operator123")
    print("Role: Operator")
    print("\nYou can now use these credentials to login as an operator")
    print("and access the operator dashboard in the mobile app!")

if __name__ == "__main__":
    main()
