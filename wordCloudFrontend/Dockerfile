#ANGULAR
FROM node:14-alpine as build

RUN mkdir -p /frontend
WORKDIR /frontend

COPY package.json /frontend

RUN npm install

COPY . /frontend
RUN npm run build 

FROM nginx:alpine

COPY --from=build /frontend/dist/word-cloud-frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
