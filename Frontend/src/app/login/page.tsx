'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/context/AuthContext';

export default function LoginPage() {
  const { login, user } = useAuth();
  const router = useRouter();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (user) {
      // Redirect to appropriate page based on user role
      if (user.role === 'admin') {
        router.push('/admin');
      } else {
        router.push('/');
      }
    }
  }, [user, router]);

  // If user is logged in, don't render the form
  if (user) {
    return null;
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      await login(formData.email, formData.password);
    } catch (error) {
      setError(error instanceof Error ? error.message : 'Login failed');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#f8fafc] px-4">
      <div className="max-w-md w-full">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-[#1e4d90] mb-2">Welcome Back</h1>
          <p className="text-gray-600">Sign in to manage your appointments</p>
        </div>
        
        <div className="bg-white p-8 rounded-lg shadow-sm">
          {error && (
            <div className="mb-4 p-3 bg-red-50 text-red-500 rounded-md text-sm">
              {error}
            </div>
          )}
          
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Email Address
              </label>
              <input
                type="text"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-[#1e4d90] focus:border-transparent text-gray-900 placeholder-gray-500"
                placeholder="doctor@example.com"
                required
                disabled={isLoading}
              />
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Password
              </label>
              <input
                type="password"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-[#1e4d90] focus:border-transparent text-gray-900 placeholder-gray-500"
                placeholder="••••••••"
                required
                disabled={isLoading}
              />
            </div>
            
            <div className="flex items-center justify-between">
              <div className="flex items-center">
                <input
                  type="checkbox"
                  id="remember"
                  className="h-4 w-4 text-[#1e4d90] focus:ring-[#1e4d90] border-gray-300 rounded"
                  disabled={isLoading}
                />
                <label htmlFor="remember" className="ml-2 block text-sm text-gray-700">
                  Remember me
                </label>
              </div>
              <Link
                href="/forgot-password"
                className="text-sm text-[#1e4d90] hover:text-[#163c72]"
              >
                Forgot password?
              </Link>
            </div>
            
            <button
              type="submit"
              className="w-full bg-[#1e4d90] text-white py-2 px-4 rounded-md hover:bg-[#163c72] transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
              disabled={isLoading}
            >
              {isLoading ? 'Signing in...' : 'Sign In'}
            </button>
          </form>
          
          <div className="mt-6 text-center text-sm">
            <span className="text-gray-600">Don&apos;t have an account?</span>{' '}
            <Link href="/signup" className="text-[#1e4d90] hover:text-[#163c72] font-medium">
              Create one now
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
} 