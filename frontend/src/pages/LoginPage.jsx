import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';

export default function LoginPage() {
  const [name, setName] = useState('');
  const [lastname, setLastname] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isRegistering, setIsRegistering] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();


  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      let result;
      if (isRegistering) {
        result = await api.post('/register', {
          name,
          lastname,
          email,
          password
        });
        alert('Usuário registrado! Agora faça o login.');
        setIsRegistering(false);
        setName('');
        setLastname('');
        setEmail('');
        setPassword('');
      } else {
        localStorage.removeItem('token');
        result = await api.post('/authenticate', {
          username: email,
          password
        });
        localStorage.setItem('token', result.data.token);
        alert('Login realizado com sucesso!');
        navigate('/main');  // <-- redireciona para a main após login
      }
    } catch (err) {
      console.error(err);
      setError('Erro ao realizar operação. Verifique as credenciais.');
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: 'auto' }}>
      <h2>{isRegistering ? 'Registrar' : 'Login'}</h2>
      <form onSubmit={handleSubmit}>
        {isRegistering && (
          <>
            <input
              type="text"
              placeholder="Nome"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            /><br />
            <input
              type="text"
              placeholder="Sobrenome"
              value={lastname}
              onChange={(e) => setLastname(e.target.value)}
              required
            /><br />
          </>
        )}
        <input
          type="email"
          placeholder="E-mail"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        /><br />
        <input
          type="password"
          placeholder="Senha"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        /><br />
        <button type="submit">
          {isRegistering ? 'Registrar' : 'Entrar'}
        </button>
      </form>
      <p>
        {isRegistering ? 'Já tem uma conta?' : 'Não tem uma conta?'}{' '}
        <button onClick={() => setIsRegistering(!isRegistering)}>
          {isRegistering ? 'Login' : 'Registrar'}
        </button>
      </p>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
}
