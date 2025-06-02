import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function RegisterPage() {
  const [form, setForm] = useState({
    name: '',
    lastname: '',
    email: '',
    password: ''
  });

  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      await axios.post('http://localhost:8080/api/v1/register', form);
      alert('Usuário registrado com sucesso! Faça login.');
      navigate('/login');
    } catch (err) {
      setError('Erro ao registrar usuário. Verifique os dados.');
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: 'auto' }}>
      <h2>Registrar</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="name"
          placeholder="Nome"
          value={form.name}
          onChange={handleChange}
          required
        /><br />
        <input
          type="text"
          name="lastname"
          placeholder="Sobrenome"
          value={form.lastname}
          onChange={handleChange}
          required
        /><br />
        <input
          type="email"
          name="email"
          placeholder="E-mail"
          value={form.email}
          onChange={handleChange}
          required
        /><br />
        <input
          type="password"
          name="password"
          placeholder="Senha"
          value={form.password}
          onChange={handleChange}
          required
        /><br />
        <button type="submit">Registrar</button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
}
