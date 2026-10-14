import js from '@eslint/js';
import tseslint from 'typescript-eslint';
import react from 'eslint-plugin-react';
import jsxA11y from 'eslint-plugin-jsx-a11y';
import jsdoc from 'eslint-plugin-jsdoc';
import importPlugin from 'eslint-plugin-import';

export default tseslint.config(
  {
    ignores: [
      '**/__test__/**',
      '**/__e2e__/**',
      '**/assets/**',
      '**/*.scss',
      '**/*.css',
      '**/*.svg',
      'build/**',
      'dist/**',
      'coverage/**',
      '.nyc_output/**',
      'node_modules/**',
      'src/services/OpenApi/**'
    ]
  },
  js.configs.recommended,
  ...tseslint.configs.recommended,
  react.configs.flat.recommended,
  react.configs.flat['jsx-runtime'],
  jsxA11y.flatConfigs.recommended,
  importPlugin.flatConfigs.recommended,
  importPlugin.flatConfigs.typescript,
  jsdoc.configs['flat/recommended'],
  {
    files: ['**/*.{js,jsx,ts,tsx}'],
    settings: {
      'import/resolver': {
        node: {
          extensions: ['.js', '.jsx', '.ts', '.tsx']
        },
        typescript: {
          alwaysTryTypes: true,
          project: './tsconfig.json'
        }
      },
      react: {
        version: 'detect'
      }
    },
    rules: {
      indent: 'off',
      'comma-dangle': 'off',
      'react/prop-types': 'off',
      'react/display-name': 'off',
      'react/no-unescaped-entities': 'off',
      'react/require-default-props': 'off',
      'linebreak-style': 0,
      'react/jsx-filename-extension': [
        2,
        {
          extensions: ['.js', '.jsx', '.ts', '.tsx']
        }
      ],
      'import/no-extraneous-dependencies': 'off',
      'import/extensions': [
        'error',
        'ignorePackages',
        {
          js: 'never',
          jsx: 'never',
          ts: 'never',
          tsx: 'never'
        }
      ],
      'import/prefer-default-export': 'off',
      'import/namespace': 'off',
      'import/no-duplicates': 'warn',
      'react/function-component-definition': [
        'error',
        {
          namedComponents: ['function-declaration', 'arrow-function'],
          unnamedComponents: 'arrow-function'
        }
      ],
      'jsx-a11y/label-has-associated-control': [
        2,
        {
          depth: 3
        }
      ],
      'no-shadow': 'off',
      'no-unused-vars': 'off',
      'no-case-declarations': 'warn',
      '@typescript-eslint/no-shadow': 'warn',
      '@typescript-eslint/no-unused-vars': [
        'error',
        {
          varsIgnorePattern: '^(_|React)',
          argsIgnorePattern: '^_',
          caughtErrorsIgnorePattern: '^_'
        }
      ],
      '@typescript-eslint/no-explicit-any': 'warn',
      '@typescript-eslint/no-non-null-asserted-optional-chain': 'warn',
      '@typescript-eslint/ban-ts-comment': 'warn',
      'jsdoc/require-param': 'off',
      'jsdoc/require-param-type': 'off',
      'jsdoc/require-returns': 'off',
      'jsdoc/require-returns-type': 'off',
      'jsdoc/require-jsdoc': 'off',
      'jsdoc/no-undefined-types': 'off',
      'jsdoc/no-defaults': 'off',
      'jsdoc/check-param-names': 'off',
      'jsdoc/check-tag-names': 'off',
      'jsdoc/escape-inline-tags': 'off',
      'jsdoc/tag-lines': 'off'
    }
  }
);
