<template>
  <div>
    <h1>User Management</h1>
    <input v-model="name" placeholder="Enter name" />
    <input v-model="email" placeholder="Enter email" />
    <button @click="createUser">Create User</button>

    <h2>Users:</h2>
    <ul>
      <li v-for="user in users" :key="user.id">
        {{ user.name }} - {{ user.email }}
        <button @click="deleteUser(user.id)">Delete</button>
        <button @click="editUser(user.id)">Edit</button>
      </li>
    </ul>
  </div>
</template>

<script>
import apiClient from '@/api/api';

export default {
  data() {
    return {
      name: '',
      email: '',
      users: [],
    };
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await apiClient.get('/users');
        this.users = response.data;
      } catch (error) {
        console.error(error);
      }
    },
    async createUser() {
      try {
        const response = await apiClient.post('/users', { name: this.name, email: this.email });
        this.users.push(response.data);
        this.name = '';
        this.email = '';
      } catch (error) {
        console.error(error);
      }
    },
    async deleteUser(id) {
      try {
        await apiClient.delete(`/users/${id}`);
        this.users = this.users.filter(user => user.id !== id);
      } catch (error) {
        console.error(error);
      }
    },
    async editUser(id) {
      const updatedUser = { name: this.name, email: this.email };
      try {
        const response = await apiClient.put(`/users/${id}`, updatedUser);
        const index = this.users.findIndex(user => user.id === id);
        this.users.splice(index, 1, response.data);
      } catch (error) {
        console.error(error);
      }
    }
  },
  mounted() {
    this.fetchUsers();
  }
};
</script>
