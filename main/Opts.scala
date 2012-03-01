/* sbt -- Simple Build Tool
 * Copyright 2011 Mark Harrah, Indrajit Raychaudhuri
 */
package sbt

/** Options for well-known tasks. */
object Opts {
	object compile {
		val deprecation = "-deprecation"
		def encoding(enc: String) = Seq("-encoding", enc)
		val explaintypes = "-explaintypes"
		val nowarn = "-nowarn"
		val optimise = "-optimise"
		val unchecked = "-unchecked"
		val verbose = "-verbose"
	}
	object doc {
		def generator(g: String) = Seq("-doc-generator", g)
		def sourceUrl(u: String) = Seq("-doc-source-url", u)
		def title(t: String) = Seq("-doc-title", t)
		def version(v: String) = Seq("-doc-version", v)
	}
}

object DefaultOptions {
	import Opts._
	import Path._
	import BuildPaths.{getGlobalBase, getGlobalSettingsDirectory}
	import Project.{Setting, extract}

	def javac: Seq[String] = compile.encoding("UTF-8")
	def scalac: Seq[String] = compile.encoding("UTF-8")
	def javadoc(name: String, version: String): Seq[String] = Seq("-doctitle", "%s %s API".format(name, version))
	def scaladoc(name: String, version: String): Seq[String] = doc.title(name) ++ doc.version(version)

	@deprecated("Use `credentials(State)` instead to make use of configuration path dynamically configured via `Keys.globalSettingsDirectory`; relying on ~/.ivy2 is not recommended anymore.", "0.12.0")
	def credentials: Credentials = Credentials(userHome / ".ivy2" / ".credentials")
	def credentials(state: State): Credentials = Credentials(getGlobalSettingsDirectory(state, getGlobalBase(state)) / ".credentials")
	def addCredentials: Setting[_] = Keys.credentials <+= Keys.state map credentials

	def shellPrompt(version: String): State => String = s => "%s:%s:%s> ".format(s.configuration.provider.id.name, extract(s).currentProject.id, version)
	def setupShellPrompt: Setting[_] = Keys.shellPrompt <<= Keys.version apply shellPrompt
}
