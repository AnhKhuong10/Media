import { ApolloClient, InMemoryCache, HttpLink } from "@apollo/client/core";

// cấu hình endpoint GraphQL
const httpLink = new HttpLink({
  uri: "http://localhost:8080/graphql", // URL backend
});

const apolloClient = new ApolloClient({
  link: httpLink,
  cache: new InMemoryCache(),
});

export default apolloClient;