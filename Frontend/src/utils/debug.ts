type LogLevel = 'info' | 'warn' | 'error' | 'debug';

interface DebugOptions {
  component?: string;
  type?: string;
  data?: any;
}

class Debug {
  private isDebugMode: boolean;

  constructor() {
    this.isDebugMode = process.env.NODE_ENV === 'development' || process.env.NEXT_PUBLIC_DEBUG_MODE === 'true';
  }

  private formatMessage(level: LogLevel, message: string, options?: DebugOptions): string {
    const timestamp = new Date().toISOString();
    const component = options?.component ? `[${options.component}]` : '';
    const type = options?.type ? `(${options.type})` : '';
    return `${timestamp} ${level.toUpperCase()} ${component}${type}: ${message}`;
  }

  private shouldLog(): boolean {
    return this.isDebugMode;
  }

  log(message: string, options?: DebugOptions) {
    if (this.shouldLog()) {
      console.log(this.formatMessage('info', message, options));
      if (options?.data) {
        console.log('Data:', options.data);
      }
    }
  }

  warn(message: string, options?: DebugOptions) {
    if (this.shouldLog()) {
      console.warn(this.formatMessage('warn', message, options));
      if (options?.data) {
        console.warn('Data:', options.data);
      }
    }
  }

  error(message: string, error?: any, options?: DebugOptions) {
    if (this.shouldLog()) {
      console.error(this.formatMessage('error', message, options));
      if (error) {
        console.error('Error:', error);
      }
      if (options?.data) {
        console.error('Data:', options.data);
      }
    }
  }

  group(name: string, options?: DebugOptions) {
    if (this.shouldLog()) {
      console.group(this.formatMessage('info', name, options));
    }
  }

  groupEnd() {
    if (this.shouldLog()) {
      console.groupEnd();
    }
  }

  time(label: string) {
    if (this.shouldLog()) {
      console.time(label);
    }
  }

  timeEnd(label: string) {
    if (this.shouldLog()) {
      console.timeEnd(label);
    }
  }

  // Auth specific debugging
  authDebug(action: string, data?: any) {
    this.log(action, {
      component: 'Auth',
      type: 'Authentication',
      data
    });
  }

  // API call debugging
  apiDebug(method: string, url: string, data?: any) {
    this.log(`${method} ${url}`, {
      component: 'API',
      type: 'Network',
      data
    });
  }

  // Component rendering debugging
  componentDebug(componentName: string, action: string, data?: any) {
    this.log(action, {
      component: componentName,
      type: 'Component',
      data
    });
  }

  // State change debugging
  stateDebug(componentName: string, stateName: string, value: any) {
    this.log(`State Update: ${stateName}`, {
      component: componentName,
      type: 'State',
      data: value
    });
  }
}

export const debug = new Debug(); 