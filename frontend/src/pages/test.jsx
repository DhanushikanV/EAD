import { useEffect, useState } from "react";

function Test() {
  const [owners, setOwners] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("/api/User")
      .then(res => res.json())
      .then(data => setOwners(data))
      .catch(err => console.error("Failed to fetch EVOwners:", err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Loading EVOwners...</p>;

  return (
    <div>
      <h1>EVOwners</h1>
      {owners.map(owner => (
        <div
          key={owner.nic}
          style={{
            border: "1px solid #ccc",
            padding: "1rem",
            margin: "0.5rem 0",
            borderRadius: "8px"
          }}
        >
          <p><strong>NIC:</strong> {owner.nic}</p>
          <p><strong>Name:</strong> {owner.name}</p>
          <p><strong>Email:</strong> {owner.email}</p>
          <p><strong>Phone:</strong> {owner.phone}</p>
          <p><strong>Status:</strong> {owner.status}</p>
          <p><strong>Created At:</strong> {new Date(owner.createdAt).toLocaleString()}</p>
        </div>
      ))}
    </div>
  );
}

export default Test;
