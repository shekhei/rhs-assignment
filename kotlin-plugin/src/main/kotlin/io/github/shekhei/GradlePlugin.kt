package io.github.shekhei

//
//open class MetaPlugin : Meta {
//    @ExperimentalContracts
//    override fun intercept(ctx: CompilerContext): List<CliPlugin> =
//            listOf(
//                    helloWorld,
//                    rhsAssignment
//            )
//}
//
//fun findPreviousSibling(el: PsiElement): PsiElement? {
//    var el: PsiElement? = el
//    while (el != null) {
//        val prev = el.getPrevSiblingIgnoringWhitespaceAndComments()
//        if (prev != null) return prev
//        el = el.parent
//    }
//    return null
//}
////
////fun findNextParameter(el: PsiElement): PsiElement {
////    val next = el.getNextSiblingIgnoringWhitespaceAndComments()
////}
//
//fun processChildren(el: PsiElement) {
//    val found = mutableListOf<PsiElement>()
//    // ever block can only have one of such operator
//    val children = el.children
//    if (children.isEmpty()) return
//    for (i in 0 until children.size - 1) {
//        val first = children[i]
//        val next = children[i + 1]
//        if (first.text == "|" && next.text == ">") {
//            // keep finding one parent with previous sibling
//            val prev = findPreviousSibling(first)
//            if (prev == null) throw IllegalStateException("syntax is wrong")
//            // lets swap them
////                var nextEl = next.nextSibling
////                while(nextEl != null) {
////                    val factory = project.elementFactory
////                    prev.parent.addBefore(prev)
////                    nextEl = nextEl.nextSibling
////                }
//            first.parent.isWritable
//            first.parent.deleteChildRange(first, first.parent.lastChild)
//        }
//        processChildren(first)
//    }
//    processChildren(children.last())
//}
//
//fun processFile(file: PsiFile) {
//    processChildren(file)
//}
//
//class OurVisitor : PsiElementVisitor() {
//    override fun visitFile(file: PsiFile?) {
////        super.visitFile(file)
//        file?.let(::processFile)
////        processFile(file)
//    }
//}
////
////internal class RhsAssignmentPreprocess : PreprocessedVirtualFileFactory() {
////    override fun CompilerContext.createPreprocessedFile(file: VirtualFile?): VirtualFile? {
////        TODO("Not yet implemented")
////
////        if (file.language is KotlinLanguage) {
//////            val file = psiFileFactory.createFileFromText(
//////                    it.virtualFilePath,
//////                    it.language,
//////                    it.text
//////            )
////
////            file.accept(OurVisitor())
////        } else {
////            file
////        }
////    }
////
////    override fun CompilerContext.createPreprocessedLightFile(file: LightVirtualFile?): LightVirtualFile? {
////        TODO("Not yet implemented")
////    }
////
////    override fun CompilerContext.isPassThrough() = false
////}
//
//internal class RhsAssignment : AnalysisHandler {
//    private var ran = false
//    // returns true if immediate children has operator
//
//
//    override fun CompilerContext.analysisCompleted(project: Project, module: ModuleDescriptor, bindingTrace: BindingTrace, files: Collection<KtFile>): AnalysisResult? {
//        if (ran) return null
//        val psiManager = PsiManager.getInstance(project)
//        val psiFileFactory = PsiFileFactory.getInstance(project)
////        files.forEach {
////            if ( it.language is KotlinLanguage) {
////                val file = psiFileFactory.createFileFromText(
////                        it.virtualFilePath,
////                        it.language,
////                        it.text
////                )
////                file.accept(OurVisitor())
////            }
//////            processFile(it)
////        }
//        return AnalysisResult.success(
//                bindingContext = bindingTrace.bindingContext,
//                module = module
//        )
//    }
//
//    override fun CompilerContext.doAnalysis(project: Project, module: ModuleDescriptor, projectContext: ProjectContext, files: Collection<KtFile>, bindingTrace: BindingTrace, componentProvider: ComponentProvider): AnalysisResult? {
//        val psiFileFactory = KtPsiFactory(project, false)
//        files.forEach {
//            if (it.language is KotlinLanguage) {
//                val file = psiFileFactory.createFile(
//                        it.text
//                )
//
//                file.accept(OurVisitor())
//            }
////            processFile(it)
//        }
//        return when (ran) {
//            true -> null
//            false -> AnalysisResult.EMPTY
//        }
//    }
//}
//
//val Meta.rhsAssignment: CliPlugin
//    get() =
//        "RhsAssignment" {
//
//            meta(
//                    RhsAssignment(),
////                    RhsAssignmentPreprocess(),
//                    blockExpression(this, {
//                        true
//                    }) { expression ->
//                        Transform.replace(
//                                replacing = expression,
//                                newDeclaration = """$statements""".block
//                        )
//                    }
//            )
//        }
//
//val Meta.helloWorld: CliPlugin
//    get() =
//        "Hello World" {
//            meta(
//                    namedFunction(this, { name == "helloWorld" }) { c ->
//                        Transform.replace(
//                                replacing = c,
//                                newDeclaration =
//                                """|fun helloWorld(): Unit =
//               |  println("Hello ΛRROW Meta!")
//               |""".function.syntheticScope
//                        )
//                    }
//            )
//        }