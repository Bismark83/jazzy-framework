// @ts-check
// `@type` JSDoc annotations allow editor autocompletion and type checking
// (when paired with `@ts-check`).
// There are various equivalent ways to declare your Docusaurus config.
// See: https://docusaurus.io/docs/api/docusaurus-config

import {themes as prismThemes} from 'prism-react-renderer';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'Jazzy Framework',
  tagline: 'A lightweight web framework for Java with a low learning curve',
  favicon: 'img/favicon.ico',

  // Set the production url of your site here
  url: 'https://jazzyframework.com',
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: '/',

  // GitHub pages deployment config.
  organizationName: 'canermastan', // Usually your GitHub org/user name.
  projectName: 'jazzy-framework', // Usually your repo name.

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang.
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: './sidebars.js',
          // Please change this to your repo.
          editUrl:
            'https://github.com/canermastan/jazzy-framework/tree/main/jazzy/docs/',
        },
        blog: {
          showReadingTime: true,
          feedOptions: {
            type: ['rss', 'atom'],
            xslt: true,
          },
          editUrl:
            'https://github.com/canermastan/jazzy-framework/tree/main/jazzy/blog/',
        },
        theme: {
          customCss: './src/css/custom.css',
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      // Replace with your project's social card
      image: 'img/docusaurus-social-card.jpg',
      navbar: {
        title: 'Jazzy Framework',
        logo: {
          alt: 'Jazzy Framework Logo',
          src: 'img/logo.svg',
        },
        items: [
          {
            type: 'docSidebar',
            sidebarId: 'tutorialSidebar',
            position: 'left',
            label: 'Documentation',
          },
          {
            to: '/docs/getting-started',
            label: 'Getting Started',
            position: 'left'
          },
          {
            to: '/docs/routing',
            label: 'Routing',
            position: 'left'
          },
          {
            to: '/docs/requests',
            label: 'Requests',
            position: 'left'
          },
          {
            to: '/docs/responses',
            label: 'Responses',
            position: 'left'
          },
          {to: '/blog', label: 'Blog', position: 'left'},
          {
            href: 'https://github.com/canermastan/jazzy-framework',
            label: 'GitHub',
            position: 'right',
          },
        ],
      },
      footer: {
        style: 'dark',
        links: [
          {
            title: 'Documentation',
            items: [
              {
                label: 'Getting Started',
                to: '/docs/getting-started',
              },
              {
                label: 'Routing',
                to: '/docs/routing',
              },
              {
                label: 'Requests',
                to: '/docs/requests',
              },
              {
                label: 'Responses',
                to: '/docs/responses',
              },
              {
                label: 'JSON Operations',
                to: '/docs/json',
              },
            ],
          },
          {
            title: 'Community',
            items: [
              {
                label: 'GitHub Discussions',
                href: 'https://github.com/canermastan/jazzy-framework/discussions',
              },
              {
                label: 'Issues',
                href: 'https://github.com/canermastan/jazzy-framework/issues',
              },
            ],
          },
          {
            title: 'More',
            items: [
              {
                label: 'Blog',
                to: '/blog',
              },
              {
                label: 'GitHub',
                href: 'https://github.com/canermastan/jazzy-framework',
              },
            ],
          },
        ],
        copyright: `Copyright © ${new Date().getFullYear()} Jazzy Framework. Built with Docusaurus.`,
      },
      prism: {
        theme: prismThemes.github,
        darkTheme: prismThemes.dracula,
        additionalLanguages: ['java'],
      },
    }),
};

export default config;
