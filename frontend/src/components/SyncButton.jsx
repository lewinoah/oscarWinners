import { useState } from 'react'

export function SyncButton({ onSyncDone, apiBase }) {
  const [syncing, setSyncing] = useState(false)
  const [message, setMessage] = useState(null)

  const handleSync = async () => {
    setSyncing(true)
    setMessage(null)
    try {
      const res = await fetch(`${apiBase}/api/movies/sync`, { method: 'POST' })
      const data = await res.json().catch(() => ({}))
      if (!res.ok) throw new Error(data.message || `HTTP ${res.status}`)
      const updated = data.updated ?? 0
      setMessage(`Synced ${updated} movie(s) from OMDb.`)
      onSyncDone?.()
    } catch (e) {
      setMessage(`Sync failed: ${e.message}`)
    } finally {
      setSyncing(false)
    }
  }

  return (
    <div>
      <button
        type="button"
        className="sync-btn"
        onClick={handleSync}
        disabled={syncing}
      >
        {syncing ? 'Syncing…' : 'Sync from OMDb'}
      </button>
      {message && (
        <div
          className={`sync-status ${message.startsWith('Synced') ? 'success' : 'error'}`}
        >
          {message}
        </div>
      )}
    </div>
  )
}
